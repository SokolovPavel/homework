package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import otus.highload.homework.core.converter.UserFromEntityConverter;
import otus.highload.homework.core.converter.UserToEntityConverter;
import otus.highload.homework.core.model.User;
import otus.highload.homework.core.persistence.entity.UserEntity;
import otus.highload.homework.core.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
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
        List<UserEntity> userEntities = userList.stream().map(user -> {
            var userEntity = toEntityConverter.convert(user);
            userEntity.setUserId(UUID.randomUUID());
            return userEntity;
        }).toList();
        log.info("To entity converted");
            userRepository.saveAll(userEntities);

    }

    @NonNull
    public List<User> search(@NonNull String firstName,@NonNull String lastName) {
        var users = userRepository.search(firstName, lastName);
        return fromEntityConverter.convertAll(users);
    }
}
