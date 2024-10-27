package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record UpdatePostRequest(@NonNull UUID id, @NonNull String text) {
}
