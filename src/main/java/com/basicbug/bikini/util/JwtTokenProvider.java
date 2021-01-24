package com.basicbug.bikini.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.basicbug.bikini.model.UserPrincipal;
import java.util.Date;

public class JwtTokenProvider {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 60 * 1000L;  // One day

    public String generateToken(UserPrincipal userPrincipal) {
        // TODO Need to fix!
        return JWT.create()
            .withSubject(userPrincipal.getUsername())
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .sign(Algorithm.HMAC256("Basicbug"));
    }
}
