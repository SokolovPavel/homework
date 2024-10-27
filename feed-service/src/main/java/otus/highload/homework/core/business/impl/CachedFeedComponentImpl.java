package otus.highload.homework.core.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import otus.highload.homework.core.business.CachedFeedComponent;
import otus.highload.homework.core.converter.PostFromEntityConverter;
import otus.highload.homework.core.model.Post;
import otus.highload.homework.core.persistence.repository.PostRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CachedFeedComponentImpl implements CachedFeedComponent {
    @NonNull
    private final PostRepository postRepository;

    @NonNull
    private final PostFromEntityConverter fromEntityConverter;

    @NonNull
    public List<Post> findFeed(@NonNull UUID currentUserId) {
        var feed = postRepository.findFeed(currentUserId, 0, 1000);
        return fromEntityConverter.convertAll(feed);
    }

}
