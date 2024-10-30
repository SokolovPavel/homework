package otus.highload.homework.core.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import otus.highload.homework.core.auth.util.JwtUtils;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    @NonNull
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            var jwt = extractToken(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                var userId = jwtUtils.getUserNameFromJwtToken(jwt);
                var userRole = jwtUtils.getUserRoleFromJwtToken(jwt);
                var userDetails = mapToUserDetails(userId, userRole);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private UserDetails mapToUserDetails(String userId, String role) {
        return new User(userId,
                "stub",
                true,
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

    private String extractToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
