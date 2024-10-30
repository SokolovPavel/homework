package otus.highload.homework.core.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity(name = "message")
@Builder
public class MessageEntity {

    @Id
    UUID id;

    @NonNull
    UUID dialogId;

    @NonNull
    UUID fromId;

    @NonNull
    UUID toId;

    @NonNull
    Instant createdAt;

    @NonNull
    String text;
}
