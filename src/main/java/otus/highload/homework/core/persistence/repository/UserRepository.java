package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;
import otus.highload.homework.core.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    @NonNull
    Optional<UserEntity> findById(@NonNull UUID userId);

    @NonNull
    List<UserEntity> findAll();

    @NonNull
    UserEntity save(@NonNull UserEntity user);

    @NonNull
    List<UserEntity> search(@NonNull String firstName, @NonNull String lastName);

    void saveAll(@NonNull List<UserEntity> userEntities);
}
