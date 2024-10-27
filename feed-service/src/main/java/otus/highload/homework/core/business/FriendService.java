package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.persistence.repository.FriendRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendService {
    @NonNull
    private final FriendRepository friendRepository;

    public void setFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId) {
        friendRepository.setFriend(currentUserId, friendUserId);
    }

    public void deleteFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId) {
        friendRepository.deleteFriend(currentUserId, friendUserId);
    }
}
