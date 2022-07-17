package com.moondysmell.gateway.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import com.moondysmell.gateway.auth.JwtUtils;
import com.moondysmell.gateway.auth.TokenUser;
import com.moondysmell.gateway.common.CommonCode;
import com.moondysmell.gateway.common.CommonResponse;
import com.moondysmell.gateway.config.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RestClient restClient;
    private final JwtUtils jwtUtils;
    private final Gson gson = new Gson();

    @Value("${uri.user-service}")
    private String userUri = "";

    public AuthController(RestClient restClient, JwtUtils jwtUtils) {
        this.restClient = restClient;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signIn")
    public CommonResponse signIn(@RequestBody HashMap<String, String> requestBody) {
        HashMap responseEntity = new HashMap<String, Object>();
        String response = restClient.restTemplatePost(userUri, "/auth/signIn", requestBody);
        try {
            responseEntity = gson.fromJson(response, HashMap.class);
//            {"code":200,"message":"성공","attribute":{"id":"62c46903aede795d338318e1","email":"momo@gmail.com"}}
            Double codeDouble = (Double) responseEntity.get("code");
            int code = codeDouble.intValue();
            switch (code) {
                case 200:
                    LinkedTreeMap attribute = (LinkedTreeMap) responseEntity.get("attribute");
                    String id = (String) attribute.get("id");
                    String email = (String) attribute.get("email");
                    String token = jwtUtils.generate(new TokenUser(id, email));
                    return new CommonResponse(CommonCode.SUCCESS, Map.of("Authorization", token));
                default:
                    return new CommonResponse(CommonCode.of((code)));
            }


        } catch (Exception e) {
            log.info(">>>  " + e);
            return new CommonResponse(CommonCode.FAIL, Map.of("message", e.getMessage()));
        }

    }
}
