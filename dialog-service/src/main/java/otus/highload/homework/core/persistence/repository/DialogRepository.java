package otus.highload.homework.core.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.entity.DialogEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DialogRepository extends CrudRepository<DialogEntity, UUID> {

    @NonNull
//    @Query("""
//            SELECT d FROM dialog d
//            WHERE (d.fromId = :fromUserId AND d.toId = :toUserId) OR (d.fromId = :toUserId AND d.toId = :fromUserId)
//            """)
    Optional<DialogEntity> findByFromIdAndToId(@NonNull UUID fromUserId, @NonNull UUID toUserId);
}
