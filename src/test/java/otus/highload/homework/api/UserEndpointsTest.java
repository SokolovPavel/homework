package otus.highload.homework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import otus.highload.homework.api.schema.LoginRequest;
import otus.highload.homework.util.Resources;

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
    private static final String USER_URL = "/user/get/{id}";
    @Autowired
    MockMvc mvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;


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
}