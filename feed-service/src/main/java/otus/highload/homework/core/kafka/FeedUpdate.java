package otus.highload.homework.core.kafka;

import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

public record FeedUpdate(@NonNull UUID userId,
                         @NonNull UUID postId,
                         @NonNull UUID authorId,
                         @NonNull String text,
                         @NonNull Instant createdAt) {
}
