package com.moondysmell.gateway.auth;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class UserJwtFilter extends AbstractGatewayFilterFactory<UserJwtFilter.Config> {

//    private static final String ROLE_KEY = "role";
    private static final String USER_ID = "userId";
    private static final String EMAIL = "email";

    private final JwtUtils jwtUtils;

    public UserJwtFilter(JwtUtils jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

//    @Override
//    public List<String> shortcutFieldOrder() {
//        return Collections.singletonList(ROLE_KEY);
//    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (!containsAuthorization(request)) {
                return onError(response, "헤더에 Authorization 토큰이 없습니다.", HttpStatus.BAD_REQUEST);
            }

            String token = extractToken(request);
            if (!jwtUtils.isValid(token)) {
                return onError(response, "Authorization 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
            }

            TokenUser tokenUser = jwtUtils.decode(token);
            addAuthorizationHeaders(request, tokenUser);
            return chain.filter(exchange);
        };
    }

    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
    }

//    private boolean hasRole(TokenUser tokenUser, String role) {
//        return role.equals(tokenUser.getRole());
//    }

    private void addAuthorizationHeaders(ServerHttpRequest request, TokenUser tokenUser) {
        request.mutate()
            .header(USER_ID, tokenUser.getId())
            .header(EMAIL, tokenUser.getEmail())
            .build();
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    //추후 config 넣고 싶으면 여기에
    @Setter
    public static class Config {
//        private String role;
    }

}