package otus.highload.homework.core.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @NonNull
    private final SimpMessagingTemplate simpMessagingTemplate;

    @NonNull
    private final SubscriptionServiceClient subscriptionServiceClient;


    public void notifySubscribers(@NonNull UUID authorId) {
        simpMessagingTemplate.convertAndSendToUser(
                "testUser", "/queue/feed", "hello");
    }

}
