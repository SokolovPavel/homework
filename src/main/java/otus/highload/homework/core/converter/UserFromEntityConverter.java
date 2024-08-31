package otus.highload.homework.core.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.lang.NonNull;
import otus.highload.homework.core.model.User;
import otus.highload.homework.core.persistence.entity.UserEntity;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface UserFromEntityConverter {

    @NonNull
    @SuppressWarnings("NullableProblems")
    User convert(@NonNull UserEntity userEntity);

    @NonNull
    @SuppressWarnings("NullableProblems")
    List<User> convertAll(@NonNull List<UserEntity> users);
}
