package otus.highload.homework.core.persistence.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import otus.highload.homework.core.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById() {
        var user = new UserEntity();
        UUID userId = UUID.randomUUID();
        user.setUserId(userId);
        String userFirstName = "testUserName";
        user.setFirstName(userFirstName);
        userRepository.save(user);
        Optional<UserEntity> foundUserOptional = userRepository.findById(userId);
        Assertions.assertTrue(foundUserOptional.isPresent());
        Assertions.assertEquals(foundUserOptional.get().getUserId(), userId);
        Assertions.assertEquals(foundUserOptional.get().getFirstName(), userFirstName);
    }

}