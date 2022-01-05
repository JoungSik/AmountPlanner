package com.joung.amount.security;

public class JwtProperties {
    // @Value("spring.jwt.secret")
    public static final String SECRET = "SomeSecretForJWTGeneration";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
