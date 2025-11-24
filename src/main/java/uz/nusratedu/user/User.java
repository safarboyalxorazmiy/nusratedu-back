package uz.nusratedu.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

/**
 * ✅ CONVERTED: User entity for blocking/servlet-based access
 *
 * Mapped to Cassandra table "users".
 * Fields represent telegram user data and authentication state.
 */
@Table("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Primary key - Telegram user ID
    @PrimaryKey
    private String telegramId;

    // User profile information
    private String username;
    private String firstName;
    private String lastName;
    private String photoUrl;

    // Login code for verification (Telegram bot flow)
    private String loginCode;
    private Instant codeExpiresAt;
    private Boolean codeUsed;

    // Timestamps
    private Instant createdAt;

    // JWT token (for backward compatibility, if needed)
    @Column("jwt_token")
    private String jwtToken;

    // Account status
    private Boolean blocked;

    // User role in the system (USER or ADMIN)
    @CassandraType(type = CassandraType.Name.TEXT)
    private UserRole role;
}