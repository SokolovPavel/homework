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

    public void createPost(@NonNull UUID currentUserId, @NonNull String text) {
        postRepository.createPost(currentUserId, text);
        //TODO: implement
    }

    public void updatePost(@NonNull UUID currentUserId, @NonNull UUID id, @NonNull String text) {
        postRepository.updatePost(id, text);

    }

    public void deletePost(@NonNull UUID currentUserId, @NonNull UUID postId) {
        postRepository.deletePost(postId);
    }

    @NonNull
    public Post findPost(@NonNull UUID postId) {
        var postEntity = postRepository.findById(postId);
        return fromEntityConverter.convert(postEntity);
    }

    @NonNull
    public Collection<Post> findFeed(@NonNull UUID currentUserId, Integer offset, Integer limit) {
        return null; //TODO: implement
    }
}
