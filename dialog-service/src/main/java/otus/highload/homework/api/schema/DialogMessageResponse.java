package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record DialogMessageResponse(@NonNull UUID from, @NonNull UUID to, @NonNull String text) {
}
