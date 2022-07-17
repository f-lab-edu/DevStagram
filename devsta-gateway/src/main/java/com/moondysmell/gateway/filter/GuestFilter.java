package com.moondysmell.gateway.filter;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GuestFilter extends AbstractGatewayFilterFactory<GuestFilter.Config> {

    public GuestFilter() {
        super(Config.class);
    }
//    private static final String USER_ID = "userId";
//    private static final String EMAIL = "email";
//    private static final String GUEST_ID = "62d3f4a5a293c66fd2d9e9dc";
//    private static final String GUEST_EMAIL = "guest@devstagram.com";


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            addAuthorizationHeaders(request);
            return chain.filter(exchange);
        };
    }


    private void addAuthorizationHeaders(ServerHttpRequest request) {
        request.mutate()
            .header("userId", "62d3f4a5a293c66fd2d9e9dc")
            .header("eamil", "guest@devstagram.com")
            .build();
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    //추후 config 넣고 싶으면 여기에
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        private String email;
    }

}