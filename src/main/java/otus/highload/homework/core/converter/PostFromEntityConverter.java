package otus.highload.homework.core.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.lang.NonNull;
import otus.highload.homework.core.model.Post;
import otus.highload.homework.core.persistence.entity.PostEntity;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface PostFromEntityConverter {

    @NonNull
    @SuppressWarnings("NullableProblems")
    Post convert(@NonNull PostEntity userEntity);

    @NonNull
    @SuppressWarnings("NullableProblems")
    List<Post> convertAll(@NonNull List<PostEntity> users);
}
