package com.turism.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("refresh", r -> r
                        // Paths to rewrite
                        .path("/users/auth/refresh")
                        // Rewrite the path to remove /keycloak-server
                        .filters(f -> f.rewritePath("/users/auth/refresh",
                                "/realms/TurismoRealm/protocol/openid-connect/token"))
                        // Forward to Keycloak server running on localhost:9000
                        .uri("http://localhost:9000"))
                .build();
    }
}
