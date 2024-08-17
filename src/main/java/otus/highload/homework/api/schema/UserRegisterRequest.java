package otus.highload.homework.api.schema;

import java.time.LocalDate;

public record UserRegisterRequest(
        String first_name,
        String second_name,
        LocalDate birthdate,
        String biography,
        String city,
        String password) {
}
