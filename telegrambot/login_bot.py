#!/usr/bin/env python3
import os
os.environ["CASSANDRA_DRIVER_NO_EXTENSIONS"] = "1"  # ensure pure-Python mode works on Py 3.13+

import random
from datetime import datetime, timedelta, timezone
from cassandra.cluster import Cluster
from telegram import Update
from telegram.ext import ApplicationBuilder, CommandHandler, ContextTypes
from dotenv import load_dotenv

# ---------------------------------------------------------------------
# 1️⃣ Load configuration
# ---------------------------------------------------------------------
load_dotenv()

TOKEN = os.getenv("TELEGRAM_BOT_TOKEN")
CASSANDRA_HOST = os.getenv("CASSANDRA_HOST", "127.0.0.1")
CASSANDRA_PORT = int(os.getenv("CASSANDRA_PORT", "9042"))
CASSANDRA_KEYSPACE = os.getenv("CASSANDRA_KEYSPACE", "nusrat")
CASSANDRA_DC = os.getenv("CASSANDRA_DC", "datacenter1")

# ---------------------------------------------------------------------
# 2️⃣ Connect to Scylla / Cassandra
# ---------------------------------------------------------------------
cluster = Cluster([CASSANDRA_HOST], port=CASSANDRA_PORT)
session = cluster.connect()

# Ensure keyspace exists
session.execute(f"""
CREATE KEYSPACE IF NOT EXISTS {CASSANDRA_KEYSPACE}
WITH replication = {{'class': 'NetworkTopologyStrategy', '{CASSANDRA_DC}': 1 }};
""")

# Connect directly into keyspace (better than USE)
session = cluster.connect(CASSANDRA_KEYSPACE)

# Ensure table exists - matching Java schema exactly
session.execute("""
CREATE TABLE IF NOT EXISTS users (
    telegramid text PRIMARY KEY,
    username text,
    firstname text,
    lastname text,
    photourl text,
    logincode text,
    codeexpiresat timestamp,
    codeused boolean,
    createdat timestamp,
    jwt_token text,
    blocked boolean,
    role text
);
""")

print("✅ Connected to Cassandra and ready!")

# ---------------------------------------------------------------------
# 3️⃣ Telegram command handlers
# ---------------------------------------------------------------------
async def login(update: Update, context: ContextTypes.DEFAULT_TYPE):
    """Generate and store a 6-digit login code."""
    user = update.effective_user
    code = f"{random.randint(0, 999999):06d}"
    expires = datetime.now(timezone.utc) + timedelta(minutes=5)

    telegram_id = str(user.id)
    username = user.username or "unknown"
    firstname = user.first_name or "NoName"
    lastname = user.last_name or ""
    codeused = False
    blocked = False
    token = None

    try:
        # 1️⃣ Check if user exists
        existing = session.execute(
            "SELECT blocked FROM users WHERE telegramid = %s;",
            (telegram_id,)
        ).one()

        if existing:
            if existing.blocked:
                await update.message.reply_text("🚫 Your account is blocked. Contact support.")
                print(f"[DB] Blocked user {telegram_id} tried to log in.")
                return
            else:
                # 2️⃣ Update login code only (do not reinsert user)
                session.execute(
                    """
                    UPDATE users
                    SET logincode = %s, codeexpiresat = %s, codeused = %s
                    WHERE telegramid = %s;
                    """,
                    (code, expires, codeused, telegram_id)
                )
                print(f"[DB] Updated existing user {telegram_id} with new code {code}")
        else:
            # 3️⃣ Insert new user if not exists
            session.execute(
                """
                INSERT INTO users (
                    telegramid, username, firstname, lastname,
                    logincode, codeexpiresat, codeused, createdat,
                    jwt_token, blocked, role, photourl
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, toTimestamp(now()), %s, %s, %s, %s);
                """,
                (telegram_id, username, firstname, lastname,
                 code, expires, codeused, token, blocked, "USER", None)
            )
            print(f"[DB] Inserted new user {telegram_id} with code {code}")

    except Exception as e:
        print("❌ DB error on insert/update:", e)
        await update.message.reply_text("⚠️ Database error. Try again later.")
        return

    await update.message.reply_text(
        f"✅ Your login code: `{code}`\n_Valid for 5 minutes._",
        parse_mode="Markdown"
    )


async def verify(update: Update, context: ContextTypes.DEFAULT_TYPE):
    """Validate the code and mark it as used."""
    user = update.effective_user
    args = context.args
    if len(args) != 1:
        await update.message.reply_text("Usage: /verify <code>")
        return

    code = args[0]
    try:
        row = session.execute(
            "SELECT logincode, codeexpiresat, codeused FROM users WHERE telegramid=%s",
            (str(user.id),)
        ).one()
    except Exception as e:
        print("❌ DB read error:", e)
        await update.message.reply_text("⚠️ Database read error.")
        return

    if not row:
        await update.message.reply_text("❌ No login request found.")
        return
    if row.codeused:
        await update.message.reply_text("⚠️ Code already used.")
        return
    if datetime.now(timezone.utc) > row.codeexpiresat:
        await update.message.reply_text("⏰ Code expired. Please /login again.")
        return
    if code != row.logincode:
        await update.message.reply_text("❌ Invalid code.")
        return

    try:
        session.execute(
            "UPDATE users SET codeused=true WHERE telegramid=%s",
            (str(user.id),)
        )
        print(f"[DB] Verified user {user.id}")
    except Exception as e:
        print("❌ DB update error:", e)
        await update.message.reply_text("⚠️ Could not update record.")
        return

    await update.message.reply_text("✅ Verified successfully!")

# ---------------------------------------------------------------------
# 4️⃣ Entry point
# ---------------------------------------------------------------------
def main():
    if not TOKEN:
        print("❌ Missing TELEGRAM_BOT_TOKEN in .env")
        return

    app = ApplicationBuilder().token(TOKEN).build()
    app.add_handler(CommandHandler("login", login))
    app.add_handler(CommandHandler("verify", verify))

    print("🤖 Bot is running...")
    app.run_polling()

if __name__ == "__main__":
    main()