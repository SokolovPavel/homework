package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;

public record SendMessageRequest(@NonNull String text) {
}
