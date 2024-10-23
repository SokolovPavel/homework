package otus.highload.homework.core.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.entity.PostEntity;
import otus.highload.homework.core.persistence.repository.PostRepository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    @Override
    public void createPost(@NonNull UUID currentUserId, @NonNull String text) {

    }

    @Override
    public void updatePost(@NonNull UUID id, @NonNull String text) {

    }

    @Override
    public void deletePost(@NonNull UUID postId) {

    }

    @NonNull
    @Override
    public PostEntity findById(@NonNull UUID postId) {
        return null;
    }
}
