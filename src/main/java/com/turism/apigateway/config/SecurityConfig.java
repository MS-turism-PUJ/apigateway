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
                                                .configurationSource(request -> {
                                                        CorsConfiguration config = new CorsConfiguration();
                                                        config.applyPermitDefaultValues();
                                                        config.addAllowedOrigin("http://webapp");
                                                        config.addAllowedOrigin("http://10.43.103.97");
                                                        return config;
                                                }))
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .authorizeExchange(exchange -> exchange
                                                .pathMatchers(HttpMethod.POST, "/users/auth/**")
                                                .permitAll()
                                                .pathMatchers("/marketplace/**")
                                                .authenticated()
                                                .pathMatchers("/services/questions/**")
                                                .hasRole("CLIENT")
                                                .pathMatchers(HttpMethod.GET, "/services/ratings/**")
                                                .authenticated()
                                                .pathMatchers("/services/ratings/**")
                                                .hasRole("CLIENT")
                                                .pathMatchers("/services/contents/{serviceId}/photo")
                                                .authenticated()
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
