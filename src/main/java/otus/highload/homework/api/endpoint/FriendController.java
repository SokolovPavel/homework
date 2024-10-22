package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otus.highload.homework.core.business.FriendService;

import java.util.UUID;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    @NonNull
    private final FriendService friendService;

    @PutMapping("/set/{userId}")
    public ResponseEntity<Void> setFriend(@PathVariable("userId") UUID friendUserId, @AuthenticationPrincipal UserDetails userDetails) {
        var currentUserId = UUID.fromString(userDetails.getUsername());
        friendService.setFriend(currentUserId, friendUserId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/detele/{userId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("userId") UUID friendUserId, @AuthenticationPrincipal UserDetails userDetails) {
        var currentUserId = UUID.fromString(userDetails.getUsername());
        friendService.deleteFriend(currentUserId, friendUserId);
        return ResponseEntity.ok().build();
    }
}
