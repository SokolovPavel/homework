package otus.highload.homework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import otus.highload.homework.api.schema.LoginRequest;
import otus.highload.homework.core.persistence.entity.UserEntity;
import otus.highload.homework.core.persistence.repository.UserRepository;
import otus.highload.homework.util.Resources;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
)
class UserEndpointsTest {

    @NonNull
    private static final Resources resources = new Resources("api/login/");
    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/user/register";
    private static final String SEARCH_URL = "/user/search";
    private static final String MASS_REGISTER_URL = "/user/register/mass";
    private static final String USER_URL = "/user/get/{id}";
    @Autowired
    MockMvc mvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Test
    @Sql("/api/login/insert-test-user.sql")
    void successfulLogin() throws Exception {
        mvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("login-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void successfulRegister() throws Exception {
        mvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("register-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_id").isNotEmpty());
    }

    @Test
    void successfulMassRegister() throws Exception {
        var multipartFile = new MockMultipartFile(
                "file",
                "users.csv",
                "text/csv",
                resources.resourceAsStream("users.csv"));
        mvc.perform(MockMvcRequestBuilders.multipart(MASS_REGISTER_URL).file(multipartFile))
                .andExpect(status().isCreated());

        List<UserEntity> loadedUsers = userRepository.findAll();
        Assertions.assertEquals(2000, loadedUsers.size());

    }

    @Test
    @Sql("/api/login/insert-test-user.sql")
    void successfulUserFlow() throws Exception {
        var responseBody = mvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("register-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        var userId = objectMapper.readTree(responseBody).get("user_id").asText();

        var loginRequest = new LoginRequest(userId, "password");

        responseBody = mvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        var token = objectMapper.readTree(responseBody).get("token").asText();
        var testUserId = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a83";
        mvc.perform(get(USER_URL, testUserId)
                        .header("Authorization", "Bearer " + token))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.user_id").value(testUserId),
                        jsonPath("$.first_name").value("tester"),
                        jsonPath("$.second_name").value("testers"),
                        jsonPath("$.biography").value("coder"),
                        jsonPath("$.city").value("saint-p"),
                        jsonPath("$.birthdate").value("2010-05-23"));
    }

    @Test
    @Sql("/api/login/insert-test-users.sql")
    void searchUsersReturnsAll() throws Exception {

        var perform = mvc.perform(get(SEARCH_URL).param("first_name", "te").param("last_name", "te"));
        perform
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$[0].user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83"),
                        jsonPath("$[0].first_name").value("tester"),
                        jsonPath("$[0].second_name").value("testers"),
                        jsonPath("$[0].biography").value("coder"),
                        jsonPath("$[0].city").value("saint-p"),
                        jsonPath("$[0].birthdate").value("2010-05-23"))
                .andExpectAll(
                        jsonPath("$[1].user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a84"),
                        jsonPath("$[1].first_name").value("test"),
                        jsonPath("$[1].second_name").value("testors"),
                        jsonPath("$[1].biography").value("coder"),
                        jsonPath("$[1].city").value("saint-p"),
                        jsonPath("$[1].birthdate").value("2010-05-23"));

    }
}