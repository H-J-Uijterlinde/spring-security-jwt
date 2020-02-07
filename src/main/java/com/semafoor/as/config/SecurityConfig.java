package com.semafoor.as.config;

import com.semafoor.as.repositories.UserRepository;
import com.semafoor.as.security.CustomAuthenticationEntryPoint;
import com.semafoor.as.security.CustomAuthenticationFilter;
import com.semafoor.as.security.CustomAuthorizationFilter;
import com.semafoor.as.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configuration class for spring security. This classed is used for creating custom beans for providing certain
 * filters in the spring security filter chain.
 *
 * Extending {@link WebSecurityConfigurerAdapter} provides some default configuration. This can be customized by
 * overriding certain methods.
 */

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@ComponentScan(basePackages = "com.semafoor.as.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * An authentication filter responds to specific requests to specific Urls. It does not provide actual authentication
     * logic, but it defines what should be done on (un)successful authentication. It delegates authentication to the
     * {@link AuthenticationManager} which must be set. A custom implementation is provided by {@link CustomAuthenticationFilter}
     *
     * @return custom authentication filter
     */

    @Bean
    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return customAuthenticationFilter;
    }

    /**
     * The authentication provider is responsible for the actual authentication. Different implementations are available
     * in this case the {@link DaoAuthenticationProvider} is used, because authentication must be done against a db.
     *
     * The DaoAuthenticationProvider performs authentication against {@link UserDetails} which it obtains from a
     * {@link UserDetailsService}
     *
     * @return {@link AuthenticationProvider}
     */

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * The authorization filter is called on every request, and is used to check for valid credentials.
     *
     * @return {@link CustomAuthorizationFilter}
     */

    @Bean
    public CustomAuthorizationFilter authorizationFilter() throws Exception{
        return new CustomAuthorizationFilter(authenticationManagerBean());
    }

    /**
     * The authentication entry point is called on certain exceptions thrown in the filter chain, before the actual
     * authentication itself. So e.g. on unauthorized calls to secured endpoints. Actual authentication errors, e.g.
     * wrong passwords, are handled elsewhere
     *
     * @return {@link CustomAuthenticationEntryPoint}
     */

    @Bean
    public CustomAuthenticationEntryPoint entryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * For database backed authentication we can provide our own {@link UserDetailsService} which returns {@link UserDetails}
     *  by looking up a user from the database, by username. The custom implementation is provided in {@link CustomUserDetailsService}
     *  UserDetails is implemented by {@link com.semafoor.as.model.User}
     *
     * @return custom userDetailsService
     */

    @Override
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    /**
     * Overriding this method allows us to configure the authentication manager builder, which is in charge of building
     * an {@link AuthenticationManager}, which is used to authenticate the requests. Here we can provide our own
     * authentication provider.
     *
     * @param auth {@link AuthenticationManagerBuilder}
     */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * Configuration of the http security. Overriding this method allows for customization of the filter chain.
     *
     * @param http {@link HttpSecurity}
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Rest, so stateless
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // add custom filters to chain
                .addFilterBefore(authorizationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // authorize all requests
                .authorizeRequests()

                // example of allowing access to specific endpoint for only certain roles.
                // this is equivalent to .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/protected/**").hasAnyRole("ADMIN", "USER")

                // example of permitting acces to certain endpoint, e.g. for registration
                .antMatchers("/open/**").permitAll()

                // secures all endpoints not specified before
                .anyRequest().authenticated()

                // allows us to hook in on exception handling by providing custom authentication entry point
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint())

                // Csrf can be disabled because we are not using cookies
                .and()
                .csrf().disable();
    }
}
