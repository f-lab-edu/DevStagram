package com.moondysmell.devstausers.domain.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//@AllArgsConstructor
@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOauth {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthToken getAccessToken(String code) throws JsonProcessingException {
        GoogleOAuthToken googleOAuthToken = objectMapper.readValue(code, GoogleOAuthToken.class);
        return googleOAuthToken;
    }

    public ResponseEntity<String> requestUserInfo(String oAuthToken) {
        String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+oAuthToken);
        log.info("Authorization: " + "Bearer "+oAuthToken);

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        log.info("response.getBody() = " + response.getBody());
        return response;
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        GoogleUser googleUser = objectMapper.readValue(userInfoRes.getBody(), GoogleUser.class);
        log.info(googleUser.toString());
        return googleUser;
    }
}