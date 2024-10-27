package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.model.DialogMessage;
import otus.highload.homework.core.persistence.repository.DialogMessagesRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DialogService {

    @NonNull
    private final DialogMessagesRepository dialogMessagesRepository;

    public void sendMessage(UUID currentUserId, UUID userId, String text) {
        //TODO: dialogMessagesRepository.save
    }

    public List<DialogMessage> getMessages(UUID currentUserId, UUID userId) {
        //TODO: dialogMessagesRepository.findBy
        return null;
    }
}
