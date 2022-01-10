package com.joung.amount.security;

import com.auth0.jwt.JWT;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtProperties {

    // @Value("spring.jwt.secret")
    public static final String SECRET = "SomeSecretForJWTGeneration";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static String createToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }
}
