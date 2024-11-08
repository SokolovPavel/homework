package otus.highload.homework.core.converter;

import org.mapstruct.*;
import org.springframework.lang.NonNull;
import otus.highload.homework.core.kafka.FeedUpdate;
import otus.highload.homework.core.persistence.entity.PostEntity;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface PostEntityToFeedUpdateConverter {

    @NonNull
    @SuppressWarnings("NullableProblems")
    @Mapping(target = "postId", source = "postEntity.id")
    FeedUpdate convert(@NonNull UUID userId, @NonNull PostEntity postEntity);

}
