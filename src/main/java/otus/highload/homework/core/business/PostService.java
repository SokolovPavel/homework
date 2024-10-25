package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.converter.PostFromEntityConverter;
import otus.highload.homework.core.model.Post;
import otus.highload.homework.core.persistence.repository.FriendRepository;
import otus.highload.homework.core.persistence.repository.PostRepository;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    @NonNull
    private final PostRepository postRepository;

    @NonNull
    private final FriendRepository friendRepository;

    @NonNull
    private final CachedFeedComponent cachedFeedService;

    @NonNull
    private final PostFromEntityConverter fromEntityConverter;

    public UUID createPost(@NonNull UUID userId, @NonNull String text) {
        var post = postRepository.createPost(userId, text);
        friendRepository.findFriends(userId)
                .forEach(cachedFeedService::evictUserFeed);
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
