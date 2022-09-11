package com.moondysmell.gateway.config;

import com.moondysmell.gateway.common.UndefiedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
@Slf4j
public class RestClient{
    @Autowired
    private RestTemplate restTemplate;
    public String restTemplatePost(String serviceName, String endpoint, HashMap<String, ?> requestBody) {
        try {
            String serviceUrl = String.format("%s%s", serviceName, endpoint);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
            log.info("restTemplate ->" + serviceUrl);
            ResponseEntity<String> restExchange = restTemplate.exchange(serviceUrl, HttpMethod.POST, httpEntity, String.class, "");
            log.info("restExchange -> " + restExchange);
            log.info("body -> " + restExchange.getBody());
            return restExchange.getBody();
        } catch (Exception e) {
            //status 200이 아닌 에러메세지들은 여기로
            log.info(">>> " + e);
            throw new UndefiedException(e.getMessage());
        }
    }

    public String restTemplateGet(String serviceName, String endpoint, HashMap<String, ?> requestBody) {
        try {
            String serviceUrl = String.format("%s%s", serviceName, endpoint);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
            log.info("restTemplate -> " + serviceUrl);
            ResponseEntity<String> restExchange = restTemplate.exchange(serviceUrl, HttpMethod.GET, httpEntity, String.class, "");
            log.info("restExchange -> " + restExchange);
            log.info("body -> " + restExchange.getBody());
            return restExchange.getBody();
        } catch (Exception e) {
            //status 200이 아닌 에러메세지들은 여기로
            log.info(">>> " + e);
            throw new UndefiedException(e.getMessage());
        }
    }
}
