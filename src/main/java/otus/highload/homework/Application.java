package otus.highload.homework;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.lang.NonNull;

@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(@NonNull String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
