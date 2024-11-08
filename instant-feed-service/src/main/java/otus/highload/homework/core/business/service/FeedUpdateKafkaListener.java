package otus.highload.homework.core.business.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import otus.highload.homework.core.kafka.FeedUpdate;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeedUpdateKafkaListener implements ConsumerSeekAware {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(
            // Определяет группу консюмера
            id = "consumer-group-1",
            // Определяет топик откуда читаем
            topics = "${kafka.topics.feed-topic}")
    public void handle(@Payload FeedUpdate message) {
        log.info("got message " + message.text() + ". to " + message.userId());
        simpMessagingTemplate.convertAndSendToUser(
                message.userId().toString(), "/queue/feed", message);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.forEach((t, o) -> callback.seekToEnd(t.topic(), t.partition()));
    }
}
