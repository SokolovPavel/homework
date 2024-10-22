package otus.highload.homework.core.persistence.repository;

import org.springframework.lang.NonNull;
import otus.highload.homework.core.persistence.entity.PostEntity;

import java.util.UUID;

public interface PostRepository {
    void createPost(@NonNull UUID currentUserId,@NonNull String text);

    void updatePost(@NonNull UUID id,@NonNull String text);

    void deletePost(@NonNull UUID postId);

    @NonNull
    PostEntity findById(@NonNull UUID postId);
}
