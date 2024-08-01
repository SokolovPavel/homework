package otus.highload.homework.core.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserEntity {

    @NonNull
    @NotBlank
    private String userId;
}
