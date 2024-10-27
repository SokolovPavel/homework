package otus.highload.homework.api.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

public record LoginRequest(
        @NonNull
        @NotBlank
        @JsonProperty("user_id")
        String userId,
        @NonNull
        @NotBlank String password) {
}
