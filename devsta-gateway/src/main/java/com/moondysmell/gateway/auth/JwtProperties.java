package com.moondysmell.gateway.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

@Setter
@Getter
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secret;

    // jwt 발급 테스트를 위해 추가함, Gateway 는 토큰 인증만 하고 발급은 처리 하지 않기 때문에 운영시엔 불필요함.
    @Value("${jwt.expiration-second}")
    private long expirationSecond;

}
