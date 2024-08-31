package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import otus.highload.homework.core.converter.UserFromEntityConverter;
import otus.highload.homework.core.converter.UserToEntityConverter;
import otus.highload.homework.core.model.User;
import otus.highload.homework.core.persistence.repository.UserRepository;

import java.util.List;
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
    @Transactional
    public UUID register(@NonNull User user) {
        var userEntity = toEntityConverter.convert(user);
        userEntity.setUserId(UUID.randomUUID());
        userEntity = userRepository.save(userEntity);
        return userEntity.getUserId();
    }

    @NonNull
    @Transactional
    public void registerAll(@NonNull List<User> userList) {
        for (User user : userList) {
            var userEntity = toEntityConverter.convert(user);
            userEntity.setUserId(UUID.randomUUID());
            userRepository.save(userEntity);
        }
    }

    @NonNull
    public List<User> search(@NonNull String firstName,@NonNull String lastName) {
        var users = userRepository.search(firstName, lastName);
        return fromEntityConverter.convertAll(users);
    }
}
