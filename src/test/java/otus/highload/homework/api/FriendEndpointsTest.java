package otus.highload.homework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import otus.highload.homework.core.persistence.entity.FriendEntity;
import otus.highload.homework.core.persistence.repository.FriendRepository;
import otus.highload.homework.util.Resources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendEndpointsTest {

    @NonNull
    private static final Resources resources = new Resources("api/friend/");
    private static final String LOGIN_URL = "/login";
    private static final String SET_FRIEND_URL = "/friend/set/{userId}";
    private static final String DELETE_FRIEND_URL = "/friend/delete/{userId}";
    @Autowired
    MockMvc mvc;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FriendRepository friendRepository;

    FriendEntityMapper friendEntityMapper = new FriendEntityMapper();

    @Test
    @Sql("/api/friend/insert-test-users.sql")
    void successfulAddFriend() throws Exception {
        var token = requestToken();
        var friendId = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a84";
        mvc.perform(put(SET_FRIEND_URL, friendId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
        var friendList = jdbcTemplate.query("SELECT * FROM friend_relation WHERE user_id = :userId",
                Map.of("userId", UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")), friendEntityMapper);
        assertThat(friendList, hasSize(1));
        assertThat(friendList.get(0).getFriendId(), is(UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a84")));
    }

    @Test
    @Sql({"/api/friend/insert-test-users.sql",
            "/api/friend/insert-friend.sql"})
    void successfulDeleteFriend() throws Exception {
        var token = requestToken();
        var friendId = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a84";
        mvc.perform(put(DELETE_FRIEND_URL, friendId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
        var friendList = jdbcTemplate.query("SELECT * FROM friend_relation WHERE user_id = :userId",
                Map.of("userId", UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")), friendEntityMapper);
        assertThat(friendList, empty());
    }

    @NonNull
    private String requestToken() throws Exception {
        MvcResult mvcResult = mvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("login-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();
        var responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("token").asText();
    }

    public static class FriendEntityMapper implements RowMapper<FriendEntity> {
        @Override
        @NonNull
        public FriendEntity mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            var friendEntity = new FriendEntity();
            friendEntity.setUserId(UUID.fromString(rs.getString("user_id")))
                    .setFriendId(UUID.fromString(rs.getString("friend_id")));
            return friendEntity;
        }
    }
}