package otus.highload.homework.core.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class UserEntity {

    @NonNull
    @NotBlank
    private UUID userId;
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
