package otus.highload.homework.core.business;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import otus.highload.homework.core.model.Post;

import java.util.List;
import java.util.UUID;

public interface CachedFeedComponent {

    @Cacheable(value = "user_feed", key = "#currentUserId")
    List<Post> findFeed(@NonNull UUID currentUserId);

    @CacheEvict(value = "user_feed", key = "#currentUserId")
    default void evictUserFeed(UUID currentUserId) {
    }
}
