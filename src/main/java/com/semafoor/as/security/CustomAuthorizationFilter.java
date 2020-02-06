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

@Slf4j
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        logger.debug("Custom authorization filter called");

        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(header);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header) {

        log.debug("Getting authentication in custom authorization filter");

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(header.replace(TOKEN_PREFIX, ""));

        String user = decodedJWT.getSubject();
        Claim roles = decodedJWT.getClaim(ROLES_CLAIM);
        String[] rolesAsString = roles.asArray(String.class);

        if (user != null) {

            log.info(Arrays.stream(rolesAsString).map(SimpleGrantedAuthority::new).collect(Collectors.toList()).toString());

            return new UsernamePasswordAuthenticationToken(user, null,
                    Arrays.stream(rolesAsString).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } else {
            return null;
        }
    }
}
