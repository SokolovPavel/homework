package otus.highload.homework.api;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import otus.highload.homework.util.Resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureEmbeddedDatabase(
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DialogEndpointTest {

    private static final String DIALOG_URL = "/dialog";

    @NonNull
    private static final Resources resources = new Resources("api/dialog/");

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(username = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")
    void postMessage() throws Exception {
        var userId = "00000000-0000-0000-0000-000000000002";
        mvc.perform(post(DIALOG_URL + "/{user_id}/send", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("message-request.json")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Sql("/api/dialog/insert-messages.sql")
    @WithMockUser(username = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")
    void getDialog() throws Exception {
        var userId = "00000000-0000-0000-0000-000000000002";
        mvc.perform(get(DIALOG_URL + "/{user_id}/list", userId))
                .andExpect(status().is2xxSuccessful());
    }
}
