package com.turism.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors
                        .configurationSource(request -> new CorsConfiguration()
                                .applyPermitDefaultValues()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/users/auth/**")
                        .permitAll()
                        // .pathMatchers(HttpMethod.POST, "/keycloak-server/realms/TurismoRealm/protocol/openid-connect/token")
                        // .permitAll()

                        .pathMatchers("/marketplace/**")
                        .hasRole("CLIENT")
                        .pathMatchers("/services/questions/**")
                        .hasRole("CLIENT")
                        .pathMatchers("/services/rating/**")
                        .hasRole("CLIENT")
                        .pathMatchers("/services/**")
                        .hasRole("PROVIDER")

                        .anyExchange().authenticated())
                // Configures JWT to properly process Keycloak tokens
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new KeycloakJwtAuthenticationConverter())))
                .build();
    }

}

