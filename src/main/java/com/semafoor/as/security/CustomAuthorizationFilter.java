package com.semafoor.as.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.semafoor.as.security.JwtConstants.*;

/**
 * Custom filter, called on every request. Checks if the request already contains a token. If so it gets the authentication
 * information from the token. Custom behaviour is provided by extending {@link BasicAuthenticationFilter} and overriding
 * the doFilterInternal method
 */

@Slf4j
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Checks the request for valid jwt token format. If so it creates a {@link UsernamePasswordAuthenticationToken} from
     * the jwt token.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        logger.debug("Custom authorization filter called");

        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(header);

        // pas token to security context. SecurityContextHolder associates right security context with right thread.
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    /**
     * Generates a {@link UsernamePasswordAuthenticationToken} from a jwt token string.
     *
     * @param header header string, containing jwt token.
     *
     * @return {@link UsernamePasswordAuthenticationToken}
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String header) {

        log.debug("Getting authentication in custom authorization filter");

        // Decodes and verifies the jwt token. Checks e.g. if token is not expired.
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(header.replace(TOKEN_PREFIX, ""));

        String user = decodedJWT.getSubject();
        Claim roles = decodedJWT.getClaim(ROLES_CLAIM);
        String[] rolesAsString = roles.asArray(String.class);

        if (user != null) {

            return new UsernamePasswordAuthenticationToken(user, null,
                    Arrays.stream(rolesAsString).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } else {
            return null;
        }
    }
}
