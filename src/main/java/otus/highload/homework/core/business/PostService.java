package otus.highload.homework.core.business;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.converter.PostFromEntityConverter;
import otus.highload.homework.core.model.Post;
import otus.highload.homework.core.persistence.repository.PostRepository;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    @NonNull
    private final PostRepository postRepository;

    @NonNull
    private final PostFromEntityConverter fromEntityConverter;

    public UUID createPost(@NonNull UUID currentUserId, @NonNull String text) {
        var post = postRepository.createPost(currentUserId, text);
        return post.getId();
    }

    public void updatePost(@NonNull UUID currentUserId, @NonNull UUID postId, @NonNull String text) {
        if (postRepository.isPostOwner(currentUserId, postId)) {
            postRepository.updatePost(postId, text);
        }

    }

    public void deletePost(@NonNull UUID currentUserId, @NonNull UUID postId) {
        if (postRepository.isPostOwner(currentUserId, postId)) {
            postRepository.deletePost(postId);
        }
    }

    @NonNull
    public Post findPost(@NonNull UUID postId) {
        var postEntityOptional = postRepository.findById(postId);
        return postEntityOptional.map(fromEntityConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @NonNull
    public Collection<Post> findFeed(@NonNull UUID currentUserId, Integer offset, Integer limit) {
        var posts = postRepository.findPosts(currentUserId, offset, limit);
        return fromEntityConverter.convertAll(posts);
    }

    static class EntityNotFoundException extends RuntimeException {

    }
}
