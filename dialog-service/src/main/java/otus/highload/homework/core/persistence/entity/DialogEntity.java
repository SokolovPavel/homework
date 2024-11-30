package otus.highload.homework.core.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.NonNull;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@RedisHash("dialog")
public class DialogEntity {

    @Id
    String id;

    @NonNull
    UUID dialogId;

    public DialogEntity(@NonNull String id) {
        this.id = id;
    }
}
