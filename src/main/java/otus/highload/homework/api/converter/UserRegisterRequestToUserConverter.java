package otus.highload.homework.api.converter;

import org.springframework.lang.NonNull;
import otus.highload.homework.api.schema.UserRegisterRequest;
import otus.highload.homework.core.model.User;

public interface UserRegisterRequestToUserConverter {

    @NonNull
    User convert(@NonNull UserRegisterRequest request);
}
