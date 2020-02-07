package com.semafoor.as.security;

import java.time.Duration;

public class JwtConstants {

    public static final long EXPIRATION_TIME = Duration.ofHours(3).toMillis();
    public static final String HEADER_STRING = "Authorization";
    public static final String HEADERS_EXPOSED = "Access-Control-Expose-Headers";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLES_CLAIM = "roles";
    public static final String SECRET = "aVerySecretStringGoesHere1234";
}
