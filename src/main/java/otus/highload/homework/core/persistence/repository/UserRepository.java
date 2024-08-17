package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;
import otus.highload.homework.core.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    @NonNull
    Optional<UserEntity> findById(@NonNull UUID userId);

    @NonNull
    UserEntity save(@NonNull UserEntity user);
}
