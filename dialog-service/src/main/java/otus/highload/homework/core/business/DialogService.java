package otus.highload.homework.core.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.converter.MessageFromEntityConverter;
import otus.highload.homework.core.model.DialogMessage;
import otus.highload.homework.core.persistence.entity.DialogEntity;
import otus.highload.homework.core.persistence.entity.MessageEntity;
import otus.highload.homework.core.persistence.repository.DialogRepository;
import otus.highload.homework.core.persistence.repository.MessageRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DialogService {

    @NonNull
    private final MessageRepository messageRepository;

    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final MessageFromEntityConverter fromEntityConverter;

    public void sendMessage(@NonNull UUID fromUserId, @NonNull UUID toUserId, @NonNull String text) {

        var dialogId = dialogRepository.findByFromIdAndToId(fromUserId, toUserId)
                .map(DialogEntity::getId)
                .orElseGet(() -> dialogRepository.save(new DialogEntity(fromUserId, toUserId)).getId());
        var message = MessageEntity.builder()
                .dialogId(dialogId)
                .fromId(fromUserId)
                .toId(toUserId)
                .createdAt(Instant.now())
                .text(text)
                .build();
        messageRepository.save(message);
    }

    @NonNull
    public List<DialogMessage> getMessages(@NonNull UUID fromUserId, @NonNull UUID toUserId) {
        var dialogId = dialogRepository.findByFromIdAndToId(fromUserId, toUserId)
                .map(DialogEntity::getId)
                .orElseThrow(EntityNotFoundException::new);
        return fromEntityConverter.convertAll(messageRepository.findAllByDialogIdOrderByCreatedAt(dialogId));
    }
}
