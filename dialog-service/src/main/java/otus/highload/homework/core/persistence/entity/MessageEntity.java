package otus.highload.homework.core.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@RedisHash("message")
@Builder
public class MessageEntity {

    @Id
    UUID id;

    @NonNull
    String dialogId;

    @NonNull
    UUID fromId;

    @NonNull
    UUID toId;

    @NonNull
    Instant createdAt;

    @NonNull
    String text;
}
