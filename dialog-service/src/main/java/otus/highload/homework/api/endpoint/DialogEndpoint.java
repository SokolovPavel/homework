package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import otus.highload.homework.api.converter.DialogMessageToResponseConverter;
import otus.highload.homework.api.schema.DialogMessageResponse;
import otus.highload.homework.api.schema.SendMessageRequest;
import otus.highload.homework.core.business.DialogService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dialog")
public class DialogEndpoint {

    @NonNull
    private final DialogService dialogService;

    @NonNull
    private final DialogMessageToResponseConverter dialogMessageToResponseConverter;

    @PostMapping("/{user_id}/send")
    ResponseEntity<Void> sendMessage(@PathVariable("user_id") @NonNull UUID userId, SendMessageRequest request) {
        var currentUserId = UUID.randomUUID();
        dialogService.sendMessage(currentUserId, userId, request.text());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{user_id}/list")
    List<DialogMessageResponse> getDialog(@PathVariable("user_id") @NonNull UUID userId) {
        var currentUserId = UUID.randomUUID();
        var messages = dialogService.getMessages(currentUserId, userId);
        return dialogMessageToResponseConverter.convertAll(messages);
    }
}
