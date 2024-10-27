package otus.highload.homework.core.model;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record DialogMessage(@NonNull UUID from, @NonNull UUID to, @NonNull String text) {
}
