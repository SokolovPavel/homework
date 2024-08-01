package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

public record LoginResponse(@NonNull @NotBlank String token) {
}
