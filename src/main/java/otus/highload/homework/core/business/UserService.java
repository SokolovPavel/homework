package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.converter.UserFromEntityConverter;
import otus.highload.homework.core.converter.UserToEntityConverter;
import otus.highload.homework.core.model.User;
import otus.highload.homework.core.persistence.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @NonNull
    private final UserToEntityConverter toEntityConverter;

    @NonNull
    private final UserFromEntityConverter fromEntityConverter;

    @NonNull
    private final UserRepository userRepository;
    
    @NonNull
    public Optional<User> findById(@NonNull UUID id) {
        return userRepository.findById(id).map(fromEntityConverter::convert);
    }

    @NonNull
    public UUID register(@NonNull User user) {
        var userEntity = toEntityConverter.convert(user);
        userEntity = userRepository.save(userEntity);
        return userEntity.getUserId();
    }
}
