package com.semafoor.as.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Overrides default behaviour on authentication- / authorization exceptions. Default behaviour is to redirect to login
 * page, which makes no sense for REST-API.
 */

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Overriding commence method allows defining behaviour upon exceptions thrown in spring security filter chain.
     * In this case returning status 401 with the message: unauthorized.
     *
     * @param request request that caused an exception.
     * @param response response to client.
     * @param authException exception thrown in filter chain.
     * @throws IOException might throw exception when writing to response.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.debug("authentication entry point called");

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
