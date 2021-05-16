package com.basicbug.bikini.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 60 * 1000L;  // One day

    public String generateToken(String userPk, List<String> roles) {
        // TODO Need to fix!
        // TODO secret key setup
        Long currentTime = System.currentTimeMillis();
        return JWT.create()
            .withClaim("roles", roles)
            .withSubject(userPk)
            .withIssuedAt(new Date(currentTime))
            .withExpiresAt(new Date(currentTime + EXPIRE_DURATION))
            .sign(Algorithm.HMAC256("Basicbug"));
    }

    public boolean isValidToken(String token) {
        return true;
    }

    private String getUserPk(String token) {
        return JWT.decode(token).getSubject();
    }
}
