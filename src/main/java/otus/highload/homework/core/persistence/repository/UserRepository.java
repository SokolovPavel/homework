package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;
import otus.highload.homework.core.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {

    @NonNull
    Optional<UserEntity> findById(@NonNull String userId);

    @NonNull
    UserEntity save(@NonNull UserEntity user);
}
