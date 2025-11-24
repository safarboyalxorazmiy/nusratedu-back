package uz.nusratedu.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * ✅ CONVERTED: SecurityUser adapter for Spring Security integration
 *
 * This class wraps the User entity to provide Spring Security's UserDetails interface.
 * Compatible with both servlet-based and virtual thread authentication.
 */
public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ✅ Converts UserRole enum to ROLE_USER or ROLE_ADMIN
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        // ✅ Using JWT token (null for stateless auth)
        // For stateless JWT auth, this can return null or the JWT token
        return null;
    }

    @Override
    public String getUsername() {
        // ✅ Using telegramId as unique identifier
        return user.getTelegramId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // ✅ Accounts never expire in this system
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // ✅ Account is locked if blocked flag is true
        return !Boolean.TRUE.equals(user.getBlocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // ✅ Credentials never expire (JWT handles expiration)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // ✅ All users are enabled by default
        return true;
    }

    /**
     * Get the wrapped User entity
     */
    public User getUser() {
        return user;
    }
}