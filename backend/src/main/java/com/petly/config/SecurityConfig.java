package com.petly.config;

import com.petly.auth.security.JwtRoleConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author farzane.rahmani
 * @created 10/12/2026
 */


@Configuration
public class SecurityConfig {


    @Bean
    public ScraperApiKeyFilter scraperApiKeyFilter(
            @Value("${petly.scraper.api-key}") String apiKey
    ) {
        return new ScraperApiKeyFilter(apiKey);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ScraperApiKeyFilter scraperApiKeyFilter,
            JwtRoleConverter jwtRoleConverter
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/otp/request",
                                "/api/v1/auth/otp/verify"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/scraper",
                                "/api/v1/scraper/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/pet-shops").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/store-owner-requests/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        scraperApiKeyFilter,
                        BearerTokenAuthenticationFilter.class
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtRoleConverter)
                        )
                );

        return http.build();
    }

}
