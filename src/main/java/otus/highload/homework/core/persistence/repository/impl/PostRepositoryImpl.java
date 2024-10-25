package otus.highload.homework.core.persistence.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.entity.PostEntity;
import otus.highload.homework.core.persistence.repository.PostRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    @NonNull
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public PostEntity createPost(@NonNull UUID currentUserId, @NonNull String text) {
        var postEntity = new PostEntity();
        postEntity.setId(UUID.randomUUID())
                .setAuthorId(currentUserId)
                .setText(text)
                .setCreatedAt(Instant.now());
        jdbcTemplate.update("""
                        INSERT INTO post(
                            id,
                            author_id, 
                            text, 
                            created_at) 
                        VALUES(:id, :authorId, :text, :createdAt)
                        """,
                Map.of("id", postEntity.getId(),
                        "authorId", postEntity.getAuthorId(),
                        "text", postEntity.getText(),
                        "createdAt", Timestamp.from(postEntity.getCreatedAt())));
        return postEntity;
    }

    public boolean isPostOwner(@NonNull UUID userId, @NonNull UUID postId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("""
                SELECT CASE 
                WHEN EXISTS(
                    SELECT 1 
                    FROM POST 
                    WHERE author_id = :userId AND id = :postId
                ) 
                THEN TRUE ELSE FALSE END AS result
                """, Map.of("userId", userId, "postId", postId), Boolean.class));
    }

    @Override
    public void updatePost(@NonNull UUID postId, @NonNull String text) {
        jdbcTemplate.update("""
                        UPDATE post
                        SET text = :text
                        WHERE id = :id
                        """,
                Map.of("id", postId,
                        "text", text
                ));
    }

    @Override
    public void deletePost(@NonNull UUID postId) {
        jdbcTemplate.update("""
                        DELETE FROM post
                        WHERE id = :id
                        """,
                Map.of("id", postId));
    }

    @NonNull
    @Override
    public Optional<PostEntity> findById(@NonNull UUID postId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("""
                SELECT * FROM post
                WHERE id = :id
                """, Map.of("id", postId), (rs, num) -> {
            var entity = new PostEntity();
            entity.setId(rs.getObject("id", UUID.class))
                    .setAuthorId(rs.getObject("author_id", UUID.class))
                    .setText(rs.getString("text"))
                    .setCreatedAt(rs.getTimestamp("created_at").toInstant());
            return entity;
        }));
    }
}
