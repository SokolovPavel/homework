package otus.highload.homework.core.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Log4j2
public class JwtUtils {
    @Value("${security.secretKey}")
    private String jwtSecret;

    @Value("${security.expirationTimeMs}")
    private int jwtExpirationMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(key()).build();
        return jwtParser
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String getUserRoleFromJwtToken(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(key()).build();
        Claims payload = jwtParser
                .parseSignedClaims(token)
                .getPayload();
        return payload.get("ROLE", String.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(key()).build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
