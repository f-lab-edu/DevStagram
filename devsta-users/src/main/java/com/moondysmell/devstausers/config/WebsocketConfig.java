package com.moondysmell.devstausers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //stomp을 사용하기 위해 선언하는 어노테이션
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat") //웹소켓 핸드세이크 커넥셕을 생성할 경로
                //.setAllowedOriginPatterns("http://*.*.*.*:8080", "http://*:9090")
                //.setAllowedOrigins("*")
                //.setAllowedOrigins("http://localhost:9090")
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setPathMatcher(new AntPathMatcher(".")); // url을 chat/room/3 -> chat.room.3으로 참조하기 위한 설정
        //registry.setApplicationDestinationPrefixes("/app"); //client에서 send요청을 처리
        registry.setApplicationDestinationPrefixes("/pub"); //client에서 send요청을 처리

        //registry.enableSimpleBroker("/sub");
        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue"); // stomp의 simplebrocker의 기능과 외부 mq(rabbimq) 에 메시지를 전달하는 기능
//
    }


}
