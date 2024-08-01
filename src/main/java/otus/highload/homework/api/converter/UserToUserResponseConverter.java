package otus.highload.homework.api.converter;

import org.springframework.lang.NonNull;
import otus.highload.homework.api.schema.UserResponse;
import otus.highload.homework.core.model.User;

public interface UserToUserResponseConverter {
    @NonNull
    UserResponse convert(@NonNull User user);
}
