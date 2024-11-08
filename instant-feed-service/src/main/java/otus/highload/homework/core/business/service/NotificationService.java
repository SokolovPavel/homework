package otus.highload.homework.core.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        log.info("subscription: " + simpDestination + ". " + message);
    }

}
