package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import otus.highload.homework.api.converter.PostToPostResponseConverter;
import otus.highload.homework.api.schema.CreatePostRequest;
import otus.highload.homework.api.schema.PostResponse;
import otus.highload.homework.api.schema.UpdatePostRequest;
import otus.highload.homework.core.business.PostService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostEndpoint {
    @NonNull
    private final PostService postService;

    @NonNull
    private final PostToPostResponseConverter postToPostResponseConverter;

    @PostMapping("/create")
    @NonNull
    public UUID createPost(@RequestBody CreatePostRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        var currentUserId = UUID.fromString(userDetails.getUsername());
        return postService.createPost(currentUserId, request.text());
    }

    @PutMapping("/update")
    @NonNull
    public ResponseEntity<Void> updatePost(@RequestBody UpdatePostRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        var currentUserId = UUID.fromString(userDetails.getUsername());
        postService.updatePost(currentUserId, request.id(), request.text());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        var currentUserId = UUID.fromString(userDetails.getUsername());
        postService.deletePost(currentUserId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public PostResponse findPost(@PathVariable("id") UUID postId) {
        var post = postService.findPost(postId);
        return postToPostResponseConverter.convert(post);
    }

    @GetMapping("/feed")
    public Collection<PostResponse> feedPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, @AuthenticationPrincipal UserDetails userDetails) {
        var currentUserId = UUID.fromString(userDetails.getUsername());
        var posts = postService.findFeed(currentUserId, offset, limit);
        return postToPostResponseConverter.convertAll(posts);
    }
}
