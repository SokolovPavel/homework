package otus.highload.homework.core.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;
import otus.highload.homework.core.persistence.entity.MessageEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisDialogRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private SetOperations<String, String> stringStringSetOperations;

    @PostConstruct
    private void init() {
        stringStringSetOperations = redisTemplate.opsForSet();
    }

    public void save(MessageEntity message) {
        try {
            String value = objectMapper.writeValueAsString(message);
            stringStringSetOperations.add(toDialogKey(message.getDialogId()), value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("cant serialize message entity");
        }

    }

    private String toDialogKey(String dialogId) {
        return "dialog:%s".formatted(dialogId);
    }

    public List<MessageEntity> findAllByDialogIdOrderByCreatedAt(String dialogId) {
        return stringStringSetOperations.members(toDialogKey(dialogId)).stream()
                .map(this::convertToMessageEntity)
                .toList();
    }

    private MessageEntity convertToMessageEntity(String value) {
        try {
            return objectMapper.readValue(value, MessageEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
