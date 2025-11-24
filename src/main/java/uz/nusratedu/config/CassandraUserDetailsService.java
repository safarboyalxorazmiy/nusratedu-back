package uz.nusratedu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.nusratedu.user.UserRepository;
import uz.nusratedu.user.SecurityUser;

@Service
@RequiredArgsConstructor
public class CassandraUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String telegramId) throws UsernameNotFoundException {
        return userRepository.findById(telegramId)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + telegramId));
    }
}