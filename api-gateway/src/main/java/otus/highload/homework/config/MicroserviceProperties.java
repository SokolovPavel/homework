package otus.highload.homework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("infrastructure")
@Configuration
@Data
public class MicroserviceProperties {
    private String userServiceHost;
    private String dialogServiceHost;
    private String postServiceHost;
    private String loginServiceHost;
    private String friendServiceHost;
}
