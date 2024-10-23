package otus.highload.homework.core.model;

import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

public record Post(@NonNull UUID postId, @NonNull UUID authorId, @NonNull String text, @NonNull Instant createdAt) {

}
