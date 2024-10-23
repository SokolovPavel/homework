package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;
import otus.highload.homework.core.persistence.entity.PostEntity;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository {
    PostEntity createPost(@NonNull UUID currentUserId, @NonNull String text);

    boolean isPostOwner(@NonNull UUID userId, @NonNull UUID postId);

    void updatePost(@NonNull UUID id, @NonNull String text);

    void deletePost(@NonNull UUID postId);

    @NonNull
    Optional<PostEntity> findById(@NonNull UUID postId);
}
