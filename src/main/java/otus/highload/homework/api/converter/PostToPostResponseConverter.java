package otus.highload.homework.api.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import otus.highload.homework.api.schema.PostResponse;
import otus.highload.homework.core.model.Post;

import java.util.Collection;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR,
        uses = PasswordEncoderMapper.class
)
public interface PostToPostResponseConverter {
    PostResponse convert(Post post);

    Collection<PostResponse> convertAll(Collection<Post> post);
}
