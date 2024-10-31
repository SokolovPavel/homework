package otus.highload.homework.core.persistence.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import otus.highload.homework.core.persistence.entity.UserEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
)
@ActiveProfiles("local")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById() {
        var user = new UserEntity();
        UUID userId = UUID.randomUUID();
        user.setUserId(userId)
                .setFirstName(RandomStringUtils.random(10))
                .setSecondName(RandomStringUtils.random(10))
                .setBiography(RandomStringUtils.random(10))
                .setCity(RandomStringUtils.random(10))
                .setBirthdate(LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(1000, 10000)))
                .setPassword(RandomStringUtils.random(10));

        userRepository.save(user);
        Optional<UserEntity> foundUserOptional = userRepository.findById(userId);
        assertThat(foundUserOptional)
                .isPresent()
                .get()
                .extracting("userId",
                        "firstName",
                        "secondName",
                        "biography",
                        "city",
                        "password",
                        "birthdate")
                .containsExactly(userId,
                        user.getFirstName(),
                        user.getSecondName(),
                        user.getBiography(),
                        user.getCity(),
                        user.getPassword(),
                        user.getBirthdate());
    }

}