package com.basicbug.bikini.auth.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRE_DURATION = 30 * 1000L;  // 2 hour
    private static final long REFRESH_TOKEN_EXPIRE_DURATION = 24 * 60 * 60 * 1000L; // One day

    private final UserDetailsService userDetailsService;

    public String createAccessToken(String userPk, List<String> roles) {
        return generateToken(userPk, roles, ACCESS_TOKEN_EXPIRE_DURATION);
    }

    public String createRefreshToken(String userPk, List<String> roles) {
        return generateToken(userPk, roles, REFRESH_TOKEN_EXPIRE_DURATION);
    }

    private String generateToken(String userPk, List<String> roles, long expireTime) {
        // TODO Need to fix!
        // TODO secret key setup
        long currentTime = System.currentTimeMillis();
        return JWT.create()
            .withClaim("roles", roles)
            .withSubject(userPk)
            .withIssuedAt(new Date(currentTime))
            .withExpiresAt(new Date(currentTime + expireTime))
            .sign(Algorithm.HMAC256("Basicbug"));
    }

    public boolean isValidToken(String token) {
        if (token == null || token.isEmpty()) return false;
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return JWT.decode(token).getExpiresAt().before(new Date());
    }

    public long getExpireTime(String token) {
        return JWT.decode(token).getExpiresAt().getTime();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserPk(String token) {
        return JWT.decode(token).getSubject();
    }
}
