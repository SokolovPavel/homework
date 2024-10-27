package otus.highload.homework.api.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.lang.NonNull;
import otus.highload.homework.api.schema.UserResponse;
import otus.highload.homework.core.model.User;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface UserToUserResponseConverter {
    @NonNull
    @SuppressWarnings("NullableProblems")
    UserResponse convert(@NonNull User user);

    @NonNull
    List<UserResponse> convertAll(@NonNull List<User> users);
}
