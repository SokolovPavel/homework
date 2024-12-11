package otus.highload.homework.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class Routing {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, MicroserviceProperties microserviceProperties) {
        return builder.routes()
                .route(p -> p.path("/user/**")
                        .filters(f -> f.addRequestHeader("request-id", UUID.randomUUID().toString()))
                        .uri(microserviceProperties.getUserServiceHost()))
                .route(p -> p.path("/login")
                        .filters(f -> f.addRequestHeader("request-id", UUID.randomUUID().toString()))
                        .uri(microserviceProperties.getLoginServiceHost()))
                .route(p -> p.path("/dialog/**")
                        .filters(f -> f.addRequestHeader("request-id", UUID.randomUUID().toString()))
                        .uri(microserviceProperties.getDialogServiceHost()))
                .route(p -> p.path("/post/**")
                        .filters(f -> f.addRequestHeader("request-id", UUID.randomUUID().toString()))
                        .uri(microserviceProperties.getPostServiceHost()))
                .route(p -> p.path("/friend/**")
                        .filters(f -> f.addRequestHeader("request-id", UUID.randomUUID().toString()))
                        .uri(microserviceProperties.getFriendServiceHost()))
                .build();
    }
}
