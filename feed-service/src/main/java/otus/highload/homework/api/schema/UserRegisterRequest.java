package otus.highload.homework.api.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserRegisterRequest(

        @NonNull
        @JsonProperty("first_name")
        @NotBlank
        String firstName,

        @NonNull
        @NotBlank
        @JsonProperty("second_name")
        String secondName,

        @NonNull
        LocalDate birthdate,

        @NonNull
        @NotBlank String biography,

        @NonNull
        @NotBlank String city,

        @NonNull
        @NotBlank String password) {
}
