package otus.highload.homework.core.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.repository.FriendRepository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    @NonNull
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public boolean deleteFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId) {
        int updateCount = jdbcTemplate.update("""
                DELETE FROM friend_relation 
                WHERE user_id = :userId AND friend_id = :friendId
                """, Map.of(
                "userId", currentUserId,
                "friendId", friendUserId
        ));
        return updateCount == 1;
    }

    @Override
    public boolean setFriend(@NonNull UUID currentUserId, @NonNull UUID friendUserId) {
        int updateCount = jdbcTemplate.update("""
                INSERT INTO friend_relation(user_id, friend_id)
                VALUES(:userId, :friendId)
                """, Map.of(
                "userId", currentUserId,
                "friendId", friendUserId
        ));
        return updateCount == 1;
    }
}
