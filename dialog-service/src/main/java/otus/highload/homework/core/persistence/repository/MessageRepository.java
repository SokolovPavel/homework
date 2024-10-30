package otus.highload.homework.core.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.highload.homework.core.persistence.entity.MessageEntity;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findAllByDialogIdOrderByCreatedAt(UUID dialogId);
}
