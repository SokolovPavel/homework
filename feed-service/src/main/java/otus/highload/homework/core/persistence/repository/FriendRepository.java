package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface FriendRepository {
    boolean deleteFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId);

    boolean setFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId);

    @NonNull
    List<UUID> findFriends(@NonNull UUID userId);

    @NonNull
    List<UUID> findSubscribers(@NonNull UUID userId);
}
