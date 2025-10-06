package uz.nusratedu.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

import java.time.Instant;

@Table("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    private String telegramId;
    private String username;
    private String firstName;
    private String lastName;
    private String photoUrl;

    private String loginCode;
    private Instant codeExpiresAt;
    private Boolean codeUsed;
    private Instant createdAt;

    @Column("jwt_token")
    private String jwtToken;
    private Boolean blocked;

    @CassandraType(type = Name.TEXT)
    private UserRole role;
}
