package otus.highload.homework.core.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "dialog")
public class DialogEntity {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    UUID id;

    @NonNull
    UUID fromId;

    @NonNull
    UUID toId;

    public DialogEntity(@NonNull UUID fromId, @NonNull UUID toId) {
        this.fromId = fromId;
        this.toId = toId;
    }
}
