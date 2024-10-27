package otus.highload.homework.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otus.highload.homework.api.schema.LoginRequest;
import otus.highload.homework.api.schema.LoginResponse;
import otus.highload.homework.core.auth.util.JwtUtils;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginEndpoint {

    @NonNull
    AuthenticationManager authenticationManager;

    @NonNull
    JwtUtils jwtUtils;

    @PostMapping
    @NonNull
    public LoginResponse loginUser(@NonNull @RequestBody LoginRequest loginRequest) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.userId(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var token = jwtUtils.generateJwtToken(authentication);
        return new LoginResponse(token);
    }
}
