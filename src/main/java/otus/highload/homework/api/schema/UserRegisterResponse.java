package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record UserRegisterResponse(@NonNull UUID userId) {
}
