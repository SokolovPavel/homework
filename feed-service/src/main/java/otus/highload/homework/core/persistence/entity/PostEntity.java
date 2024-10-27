package otus.highload.homework.core.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class PostEntity {

    @NonNull
    private UUID id;

    @NonNull
    @NotBlank
    private UUID authorId;

    @NonNull
    private String text;

    @NonNull
    private Instant createdAt;
}
