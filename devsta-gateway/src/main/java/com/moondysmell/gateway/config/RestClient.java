package com.moondysmell.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
@Slf4j
public class RestClient{
    private RestTemplate restTemplate;
    public String restTemplatePost(String serviceName, String endpoint, HashMap<String, ?> requestBody) {
        try {
            String serviceUrl = String.format("%s%s", serviceName, endpoint);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
            log.info("restTemplate -> $serviceUrl");
            ResponseEntity<String> restExchange = restTemplate.exchange(serviceUrl, HttpMethod.POST, httpEntity, String.class, "");
            log.info("restExchange -> $restExchange");
            log.info("body -> ${restExchange.body}");
            return restExchange.getBody();
        } catch (Exception e) {
            log.info(">>> " + e);
            return null;
        }
    }
}
