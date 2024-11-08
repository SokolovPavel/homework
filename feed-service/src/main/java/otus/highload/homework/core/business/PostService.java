package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.converter.PostEntityToFeedUpdateConverter;
import otus.highload.homework.core.converter.PostFromEntityConverter;
import otus.highload.homework.core.kafka.FeedUpdate;
import otus.highload.homework.core.model.Post;
import otus.highload.homework.core.persistence.repository.FriendRepository;
import otus.highload.homework.core.persistence.repository.PostRepository;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    @NonNull
    private final PostRepository postRepository;

    @NonNull
    private final FriendRepository friendRepository;

    @NonNull
    private final CachedFeedComponent cachedFeedService;

    @NonNull
    private final PostFromEntityConverter fromEntityConverter;

    @NonNull
    private final PostEntityToFeedUpdateConverter postEntityToFeedUpdateConverter;
    @NonNull
    private final KafkaTemplate<String, FeedUpdate> kafkaTemplate;

    @Value("${kafka.topics.feed-topic:}")
    private String feedTopic;

    @Value("${kafka.instant-feed-update-threshold")
    private int feedUpdateThreshold;

    public UUID createPost(@NonNull UUID userId, @NonNull String text) {
        var post = postRepository.createPost(userId, text);
        var counter = new AtomicInteger();
        friendRepository.findSubscribers(userId)
                .forEach(friendId -> {
                    if (counter.getAndIncrement() < feedUpdateThreshold) {
                        log.info("sending " + text + " to user " + friendId);
                        kafkaTemplate.send(feedTopic, postEntityToFeedUpdateConverter.convert(friendId, post));
                    }
                    cachedFeedService.evictUserFeed(friendId);
                });
        return post.getId();
    }

    public void updatePost(@NonNull UUID userId, @NonNull UUID postId, @NonNull String text) {
        if (postRepository.isPostOwner(userId, postId)) {
            postRepository.updatePost(postId, text);
            friendRepository.findFriends(userId)
                    .forEach(cachedFeedService::evictUserFeed);
        }

    }

    public void deletePost(@NonNull UUID userId, @NonNull UUID postId) {
        if (postRepository.isPostOwner(userId, postId)) {
            postRepository.deletePost(postId);
            friendRepository.findSubscribers(userId)
                    .forEach(cachedFeedService::evictUserFeed);
        }
    }

    @NonNull
    public Post findPost(@NonNull UUID postId) {
        var postEntityOptional = postRepository.findById(postId);
        return postEntityOptional.map(fromEntityConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @NonNull
    public Collection<Post> findFeed(@NonNull UUID currentUserId, Integer offset, Integer limit) {
        return cachedFeedService.findFeed(currentUserId).stream()
                .skip(offset)
                .limit(limit)
                .toList();
    }

    static class EntityNotFoundException extends RuntimeException {

    }
}
