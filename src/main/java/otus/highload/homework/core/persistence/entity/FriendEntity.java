package otus.highload.homework.core.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class FriendEntity {
    @NonNull
    @NotBlank
    private UUID userId;

    @NonNull
    @NotBlank
    private UUID friendId;
}
