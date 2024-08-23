package otus.highload.homework.api.converter;

import org.mapstruct.*;
import org.springframework.lang.NonNull;
import otus.highload.homework.api.schema.UserRegisterRequest;
import otus.highload.homework.core.model.User;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR,
        uses = PasswordEncoderMapper.class
)
public interface UserRegisterRequestToUserConverter {

    @NonNull
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    @SuppressWarnings("NullableProblems")
    User convert(@NonNull UserRegisterRequest request);
}
