package otus.highload.homework.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import otus.highload.homework.core.persistence.entity.PostEntity;
import otus.highload.homework.core.persistence.repository.PostRepository;
import otus.highload.homework.core.persistence.repository.UserRepository;
import otus.highload.homework.util.Resources;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
)
@ActiveProfiles("local")
class PostEndpointsTest {

    @NonNull
    private static final Resources resources = new Resources("api/post/");
    private static final String CREATE_URL = "/post/create";
    private static final String GET_URL = "/post/get/{id}";
    private static final String UPDATE_URL = "/post/update";
    private static final String DELETE_URL = "/post/delete/{id}";
    private static final String FEED_URL = "/post/feed";
    private static final String LOGIN_URL = "/login";
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;


    @SpyBean
    PostRepository postRepository;

    @Test
    @Sql("/api/post/insert-test-user.sql")
    void successfulCreatePost() throws Exception {
        var token = requestToken();
        var postId = mvc.perform(post(CREATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(resources.loadAsBytes("create-post-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        var friendList = jdbcTemplate.query("SELECT * FROM post WHERE id = :postId",
                Map.of("postId", UUID.fromString(postId.substring(1, postId.length() - 1))), PostEntityRowMapper());
        assertThat(friendList, hasSize(1));
        assertThat(friendList.get(0).getAuthorId(), is(UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")));
        assertThat(friendList.get(0).getText(), is("Test post creation!"));
    }

    @Test
    @Sql({"/api/post/insert-test-user.sql", "/api/post/insert-test-post.sql"})
    void successfulUpdatePost() throws Exception {
        var token = requestToken();
        mvc.perform(put(UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(resources.loadAsBytes("update-post-request.json")))
                .andExpect(status().is2xxSuccessful());

        var friendList = jdbcTemplate.query("SELECT * FROM post WHERE id = '8076b3bc-bfc7-458a-8d3d-c9c2e5436a85'", PostEntityRowMapper());
        assertThat(friendList, hasSize(1));
        assertThat(friendList.get(0).getAuthorId(), is(UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")));
        assertThat(friendList.get(0).getText(), is("Updated text"));
    }

    @Test
    @Sql({"/api/post/insert-test-user.sql", "/api/post/insert-test-post.sql"})
    void successfulDeletePost() throws Exception {
        var token = requestToken();
        var postId = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a85";
        mvc.perform(delete(DELETE_URL, postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());

        var friendList = jdbcTemplate.query("SELECT * FROM post WHERE id = '8076b3bc-bfc7-458a-8d3d-c9c2e5436a85'", PostEntityRowMapper());
        assertThat(friendList, empty());
    }

    @Test
    @Sql({"/api/post/insert-test-user.sql", "/api/post/insert-test-post.sql"})
    void getPostReturnsCorrectPost() throws Exception {
        var token = requestToken();
        var postId = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a85";
        mvc.perform(get(GET_URL, postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a85"))
                .andExpect(jsonPath("$.text").value("Awesome post"))
                .andExpect(jsonPath("$.author_user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83"));
    }

    @Test
    @Sql("/api/post/insert-data-for-feed.sql")
    @DirtiesContext
    void getPostFeedReturnsCorrectData() throws Exception {
        var token = requestToken();
        mvc.perform(get(FEED_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].id").value("8076b3bc-bfc7-458a-8d3d-c9c2e0000006"))
                .andExpect(jsonPath("$[0].text").value("Awesome post6"))
                .andExpect(jsonPath("$[0].author_user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a85"))
                .andExpect(jsonPath("$[1].id").value("8076b3bc-bfc7-458a-8d3d-c9c2e0000005"))
                .andExpect(jsonPath("$[1].text").value("Awesome post5"))
                .andExpect(jsonPath("$[1].author_user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a85"))
                .andExpect(jsonPath("$[2].id").value("8076b3bc-bfc7-458a-8d3d-c9c2e0000004"))
                .andExpect(jsonPath("$[2].text").value("Awesome post4"))
                .andExpect(jsonPath("$[2].author_user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a84"))
                .andExpect(jsonPath("$[3].id").value("8076b3bc-bfc7-458a-8d3d-c9c2e0000003"))
                .andExpect(jsonPath("$[3].text").value("Awesome post3"))
                .andExpect(jsonPath("$[3].author_user_id").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a84"));
    }

    @Test
    @Sql("/api/post/insert-data-for-feed.sql")
    @DirtiesContext
    void getPostFeedCached() throws Exception {
        clearInvocations(postRepository);
        var token = requestToken();
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(4)));
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(4)));
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(4)));
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(4)));

        verify(postRepository, times(1)).findFeed(ArgumentMatchers.any(), eq(0), eq(1000));
    }

    @Test
    @Sql("/api/post/insert-data-for-feed.sql")
    @DirtiesContext
    void getPostFeedInvalidatedAfterPostDelete() throws Exception {
        clearInvocations(postRepository);
        var token = requestToken();
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(4)));
        deletePostRequest(requestFriendToken(), "8076b3bc-bfc7-458a-8d3d-c9c2e0000006");
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(3)));
        getFeedRequest(token).andExpect(jsonPath("$", hasSize(3)));

        verify(postRepository, times(2)).findFeed(ArgumentMatchers.any(), eq(0), eq(1000));
    }

    @NonNull
    private ResultActions deletePostRequest(@NonNull String token, @NonNull String postId) throws Exception {
        return mvc.perform(delete(DELETE_URL, postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
    }

    @NonNull
    private ResultActions getFeedRequest(@NonNull String token) throws Exception {
        return mvc.perform(get(FEED_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
    }

    @NonNull
    private RowMapper<PostEntity> PostEntityRowMapper() {
        return (rs, num) -> {
            var entity = new PostEntity();
            entity.setId(rs.getObject("id", UUID.class))
                    .setAuthorId(rs.getObject("author_id", UUID.class))
                    .setText(rs.getString("text"))
                    .setCreatedAt(rs.getTimestamp("created_at").toInstant());
            return entity;
        };
    }

    @NonNull
    private String requestFriendToken() throws Exception {
        MvcResult mvcResult = mvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("friend-login-request.json")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();
        var responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("token").asText();
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
}