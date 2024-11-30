package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.converter.MessageFromEntityConverter;
import otus.highload.homework.core.model.DialogMessage;
import otus.highload.homework.core.persistence.entity.MessageEntity;
import otus.highload.homework.core.persistence.repository.DialogRepository;
import otus.highload.homework.core.persistence.repository.RedisDialogRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DialogService {

    @NonNull
    private final RedisDialogRepository messageRepository;

    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final MessageFromEntityConverter fromEntityConverter;

    public void sendMessage(@NonNull UUID fromUserId, @NonNull UUID toUserId, @NonNull String text) {

        var dialogId = convertToDialogId(fromUserId, toUserId);
        var message = MessageEntity.builder()
                .dialogId(dialogId)
                .fromId(fromUserId)
                .toId(toUserId)
                .createdAt(Instant.now())
                .text(text)
                .build();
        messageRepository.save(message);
    }

    private String convertToDialogId(UUID fromId, UUID toId) {
        if (fromId.compareTo(toId) < 0) {
            return fromId.toString() + "-" + toId.toString();
        } else {
            return toId.toString() + "-" + fromId.toString();
        }
    }

    @NonNull
    public List<DialogMessage> getMessages(@NonNull UUID fromUserId, @NonNull UUID toUserId) {
        var dialogId = convertToDialogId(fromUserId, toUserId);
        return fromEntityConverter.convertAll(messageRepository.findAllByDialogIdOrderByCreatedAt(dialogId));
    }

    public class EntityNotFoundException extends RuntimeException {

    }
}
