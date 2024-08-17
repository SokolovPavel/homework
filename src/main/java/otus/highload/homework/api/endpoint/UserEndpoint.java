package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import otus.highload.homework.api.converter.UserRegisterRequestToUserConverter;
import otus.highload.homework.api.converter.UserToUserResponseConverter;
import otus.highload.homework.api.schema.UserRegisterRequest;
import otus.highload.homework.api.schema.UserRegisterResponse;
import otus.highload.homework.api.schema.UserResponse;
import otus.highload.homework.core.business.UserService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;

    private final UserToUserResponseConverter userToUserResponseConverter;

    private final UserRegisterRequestToUserConverter userRegisterRequestToUserConverter;

    @GetMapping("/get/{id}")
    public Optional<UserResponse> findUserById(@PathVariable @NonNull UUID id){
        return userService.findById(id)
                .map(userToUserResponseConverter::convert);
    }

    @PostMapping("/register")
    public UserRegisterResponse registerUser(@RequestBody UserRegisterRequest request){
        var user = userRegisterRequestToUserConverter.convert(request);
        var userId = userService.register(user);
        return new UserRegisterResponse(userId);
    }
}
