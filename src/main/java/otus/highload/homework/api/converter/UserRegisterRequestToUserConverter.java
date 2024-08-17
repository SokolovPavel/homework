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
    @Mapping(source = "first_name", target = "firstName")
    @Mapping(source = "second_name", target = "secondName")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User convert(@NonNull UserRegisterRequest request);
}
