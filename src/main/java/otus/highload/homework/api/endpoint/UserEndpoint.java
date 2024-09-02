package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import otus.highload.homework.api.converter.CsvFileToUserListConverter;
import otus.highload.homework.api.converter.UserRegisterRequestToUserConverter;
import otus.highload.homework.api.converter.UserToUserResponseConverter;
import otus.highload.homework.api.schema.UserRegisterRequest;
import otus.highload.homework.api.schema.UserRegisterResponse;
import otus.highload.homework.api.schema.UserResponse;
import otus.highload.homework.core.business.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserEndpoint {

    private final UserService userService;

    private final UserToUserResponseConverter userToUserResponseConverter;

    private final UserRegisterRequestToUserConverter userRegisterRequestToUserConverter;

    private final CsvFileToUserListConverter csvFileToUserListConverter;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('USER')")
    public Optional<UserResponse> findUserById(@PathVariable @NonNull UUID id) {
        return userService.findById(id)
                .map(userToUserResponseConverter::convert);
    }

    @PostMapping("/register")
    public UserRegisterResponse registerUser(@RequestBody @NonNull UserRegisterRequest request) {
        var user = userRegisterRequestToUserConverter.convert(request);
        var userId = userService.register(user);
        return new UserRegisterResponse(userId);
    }

    @PostMapping("/register/mass")
    public ResponseEntity registerUser(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("mass register start");
        var users = csvFileToUserListConverter.convertAll(file.getInputStream());
        log.info("input file converted");
        userService.registerAll(users);
        log.info("done");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public List<UserResponse> searchUsers(@RequestParam("first_name") @NonNull String firstName,
                                          @RequestParam("last_name") @NonNull String lastName) {
        var users = userService.search(firstName, lastName);
        return userToUserResponseConverter.convertAll(users);
    }
}
