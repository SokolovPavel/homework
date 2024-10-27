package otus.highload.homework.api.converter;

import org.mapstruct.*;
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
    @Mapping(target = "id", source = "postId")
    @Mapping(target = "authorUserId", source = "authorId")
    PostResponse convert(Post post);

    Collection<PostResponse> convertAll(Collection<Post> post);
}
