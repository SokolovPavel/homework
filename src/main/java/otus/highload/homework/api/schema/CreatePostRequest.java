package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;

public record CreatePostRequest(@NonNull String text) {
}
