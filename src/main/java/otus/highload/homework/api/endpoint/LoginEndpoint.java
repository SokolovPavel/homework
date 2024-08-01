package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otus.highload.homework.api.schema.LoginRequest;
import otus.highload.homework.api.schema.LoginResponse;
import otus.highload.homework.core.business.LoginService;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginEndpoint {

    @NonNull
    private final LoginService loginService;

    @PostMapping
    public LoginResponse loginUser(@NonNull @RequestBody LoginRequest loginRequest){
        //Optional loginService.tryLogin(loginRequest.userId(), loginRequest.password())
        return null;
    }
}
