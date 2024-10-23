package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;

import java.util.UUID;

public interface FriendRepository {
    boolean deleteFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId);

    boolean setFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId);
}
