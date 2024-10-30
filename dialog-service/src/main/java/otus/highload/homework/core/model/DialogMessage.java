package otus.highload.homework.core.model;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record DialogMessage(@NonNull UUID fromId, @NonNull UUID toId, @NonNull String text) {
}
