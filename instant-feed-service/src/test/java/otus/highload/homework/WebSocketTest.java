package otus.highload.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import otus.highload.homework.core.business.service.FeedUpdateKafkaListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({
        FeedUpdateKafkaListener.class
})
public class WebSocketTest {
    private static final String SUBSCRIBE_USER_FEED_ENDPOINT = "/user/testUser/queue/feed";
    private static final String URL = "/app/secured/room";
    @Value("${local.server.port}")
    private int port;
    private CompletableFuture<String> completableFuture = new CompletableFuture<>();

    @Test
    @WithMockUser(username = "userName")
    public void webSocketPingPongTest() throws InterruptedException, ExecutionException, TimeoutException {

        var WSURL = "ws://localhost:" + port + "/ws";

        SockJsClient webSocketClient = new SockJsClient(createTransportClient());
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSession stompSession = stompClient.connectAsync(WSURL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(SUBSCRIBE_USER_FEED_ENDPOINT, new SubscriptionStompFrameHandler());
        stompSession.send(URL, "ping-pong");

        var message = completableFuture.get(3, SECONDS);

        stompClient.stop();
        System.out.println(message);
        assertNotNull(message);
        assertEquals("ping-pong", message);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(webSocketClient);
        transports.add(webSocketTransport);
        return transports;
    }

    private class SubscriptionStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            System.out.println(stompHeaders.toString());
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object message) {
            System.out.println(message);
            completableFuture.complete((String) message);
        }
    }
}
