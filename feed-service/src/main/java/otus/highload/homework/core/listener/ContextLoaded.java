package otus.highload.homework.core.listener;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import otus.highload.homework.core.business.CachedFeedComponent;
import otus.highload.homework.core.persistence.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ContextLoaded implements ApplicationListener<ContextRefreshedEvent> {
    @NonNull
    private final CachedFeedComponent cachedFeedComponent;

    @NonNull
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        warmUpCache();
    }

    private void warmUpCache() {
        userRepository.findTopActiveUserIds()
                .forEach(cachedFeedComponent::findFeed);
    }
}

