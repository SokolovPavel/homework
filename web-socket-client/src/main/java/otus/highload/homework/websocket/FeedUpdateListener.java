package otus.highload.homework.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeedUpdateListener implements InitializingBean {

    private final ObjectMapper objectMapper;
    @Value("${websocket.connect-url}")
    private String connectUrl;
    @Value("${websocket.subscribe-user-id}")
    private String subscribeUserId;

    public void afterPropertiesSet() throws Exception {
        log.info("initializing listener");
        SockJsClient webSocketClient = new SockJsClient(createTransportClient());
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter(objectMapper);
        stompClient.setMessageConverter(messageConverter);

        StompSession stompSession = stompClient.connectAsync(connectUrl, new StompSessionHandlerAdapter() {
        }).get(3, SECONDS);
        log.info("initialized stomp session. " + stompSession.getSessionId());
        var subscribe = stompSession.subscribe("/user/" + subscribeUserId + "/queue/feed", new SubscriptionStompFrameHandler());
        log.info("subscription id. " + subscribe.getSubscriptionId());
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(webSocketClient);
        transports.add(webSocketTransport);
        return transports;
    }

    private static class SubscriptionStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            log.info("headers2: " + stompHeaders.toString());
            return FeedUpdate.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object message) {
            log.info("message : " + message.toString());
        }
    }
}
