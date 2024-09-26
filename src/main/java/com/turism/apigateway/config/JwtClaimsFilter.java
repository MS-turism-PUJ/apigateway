package com.turism.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtClaimsFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        if (requestPath.startsWith("/keycloak-server")) {
            return chain.filter(exchange);
        }
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .flatMap(authentication -> {
                    Jwt jwt = (Jwt) authentication.getCredentials();

                    // Extraer el "preferred_username" del JWT
                    String preferredUsername = jwt.getClaimAsString("preferred_username");

                    // Modificar la solicitud agregando los headers
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-Preferred-Username", preferredUsername) // Agregar el username a los headers
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

