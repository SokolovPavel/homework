package otus.highload.homework.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.entity.DialogEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DialogRepository extends JpaRepository<DialogEntity, UUID> {

    @NonNull
    Optional<DialogEntity> findByFromIdAndToId(@NonNull UUID fromUserId, @NonNull UUID toUserId);
}
