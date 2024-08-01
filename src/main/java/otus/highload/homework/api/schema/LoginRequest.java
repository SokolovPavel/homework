package otus.highload.homework.api.schema;

import org.springframework.lang.NonNull;
import javax.validation.constraints.NotBlank;

public record LoginRequest(@NonNull @NotBlank String userId, @NonNull @NotBlank String password) {
}
