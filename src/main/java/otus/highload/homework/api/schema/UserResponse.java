package otus.highload.homework.api.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class UserResponse {

    @NonNull
    @NotBlank
    @JsonProperty("user_id")
    private String userId;

    @NonNull
    @NotBlank
    @JsonProperty("first_name")
    private String firstName;

    @NonNull
    @NotBlank
    @JsonProperty("second_name")
    private String secondName;

    @NonNull
    private LocalDate birthdate;

    @NonNull
    private String biography;

    @NonNull
    private String city;
}
