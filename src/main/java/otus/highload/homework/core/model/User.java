package otus.highload.homework.core.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Builder
public class User {
    @NonNull
    @NotBlank
    private String userId;
    @NonNull
    @NotBlank
    private String password;
    @NonNull
    @NotBlank
    private String firstName;
    @NonNull
    @NotBlank
    private String secondName;
    @NonNull
    private LocalDate birthdate;
    @NonNull
    private String biography;
    @NonNull
    private String city;
}
