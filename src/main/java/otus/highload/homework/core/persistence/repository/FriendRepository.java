package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;

import java.util.UUID;

public interface FriendRepository {
    void deleteFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId);

    void setFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId);
}
