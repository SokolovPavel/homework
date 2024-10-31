package otus.highload.homework.api;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.lang.NonNull;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import otus.highload.homework.util.Resources;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @WithMockUser(username = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")
    void postMessage() throws Exception {
        var userId = "00000000-0000-0000-0000-000000000002";
        mvc.perform(post(DIALOG_URL + "/{user_id}/send", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resources.loadAsBytes("message-request.json")))
                .andExpect(status().is2xxSuccessful());
        var dialogRowList = jdbcClient.sql("SELECT * FROM dialog")
                .query()
                .listOfRows();
        assertThat(dialogRowList)
                .size().isEqualTo(1);
        var dialogRow = dialogRowList.get(0);
        assertThat(dialogRow)
                .containsEntry("from_id", UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83"))
                .containsEntry("to_id", UUID.fromString("00000000-0000-0000-0000-000000000002"));

        var dialogId = (UUID) dialogRow.get("id");

        var messageRowList = jdbcClient.sql("SELECT * FROM message")
                .query()
                .listOfRows();
        assertThat(messageRowList)
                .size().isEqualTo(1);
        var messageRow = messageRowList.get(0);
        assertThat(messageRow)
                .containsEntry("dialog_id", dialogId)
                .containsEntry("from_id", UUID.fromString("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83"))
                .containsEntry("to_id", UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .containsEntry("text", "Hello, friend. Didnt see you around awhile. How r U?");
    }

    @Test
    @Sql("/api/dialog/insert-messages.sql")
    @WithMockUser(username = "8076b3bc-bfc7-458a-8d3d-c9c2e5436a83")
    void getDialog() throws Exception {
        var userId = "00000000-0000-0000-0000-000000000002";
        mvc.perform(get(DIALOG_URL + "/{user_id}/list", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].from").value("00000000-0000-0000-0000-000000000002"))
                .andExpect(jsonPath("$[0].to").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83"))
                .andExpect(jsonPath("$[0].text").value("hi there"))
                .andExpect(jsonPath("$[1].from").value("8076b3bc-bfc7-458a-8d3d-c9c2e5436a83"))
                .andExpect(jsonPath("$[1].to").value("00000000-0000-0000-0000-000000000002"))
                .andExpect(jsonPath("$[1].text").value("hello"));
    }
}
