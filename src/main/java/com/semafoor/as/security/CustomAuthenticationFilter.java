package com.semafoor.as.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.semafoor.as.security.JwtConstants.*;

/**
 * Custom implementation of the authentication filter, overriding the default behaviour of springs
 * UsernamePasswordAuthenticationFilter on successful and unsuccessful authentication.
 *
 * By default this filter is activated on http post requests to /login
 */

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    ObjectMapper mapper = new ObjectMapper();

    /**
     * Defines behaviour upon successful authentication. In this case creating a jwt token and adding it to the response.
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param chain {@link FilterChain}
     * @param authResult {@link Authentication}
     */

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        log.debug("Custom authentication filter was called, attempting to create token");

        String token = JWT.create()
                .withSubject(((UserDetails) authResult.getPrincipal()).getUsername())
                // Send roles in jwt body, can be useful for clients
                .withArrayClaim(ROLES_CLAIM, authResult.getAuthorities().stream()
                        .map(GrantedAuthority::toString).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));


        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        // expose the authorization header, else clients might not be able to use it
        response.addHeader(HEADERS_EXPOSED, HEADER_STRING);
    }

    /**
     * Defines behaviour upon unsuccessful authentication attempts. For a REST-API a 401 response with JSON body makes
     * most sense
     *
     * @param request  authentication request
     * @param response response to client
     * @param failed   authentication exception
     * @throws IOException might trow IOException when writing to response
     */

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        log.info("Custom authentication failure handler called");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // generate response body
        Map<String, Object> data = new HashMap<>();
        data.put(
                "timestamp",
                Calendar.getInstance().getTime());
        data.put(
                "exception",
                failed.getMessage());

        response.getOutputStream()
                .println(mapper.writeValueAsString(data));
    }
}
