package otus.highload.homework.core.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.persistence.entity.UserEntity;
import otus.highload.homework.core.persistence.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String userId) throws UsernameNotFoundException {
        return userRepository.findById(UUID.fromString(userId))
                .map(this::mapToUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with userId " + userId + " not found"));
    }

    private UserDetails mapToUser(UserEntity userEntity) {
        return new User(userEntity.getUserId().toString(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
