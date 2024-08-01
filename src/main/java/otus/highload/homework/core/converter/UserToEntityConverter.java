package otus.highload.homework.core.converter;

import org.springframework.lang.NonNull;
import otus.highload.homework.core.model.User;
import otus.highload.homework.core.persistence.entity.UserEntity;

public interface UserToEntityConverter {
    @NonNull
    UserEntity convert(@NonNull User user);
}
