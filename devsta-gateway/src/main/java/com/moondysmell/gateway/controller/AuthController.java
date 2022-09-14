package com.moondysmell.gateway.controller;

import com.moondysmell.gateway.service.AuthService;
import com.moondysmell.gateway.auth.JwtUtils;
import com.moondysmell.gateway.common.CommonResponse;
import com.moondysmell.gateway.config.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {
    private final RestClient restClient;
    private final AuthService authService;
    public static final String SIGN_IN = "signIn";
    public static final String SIGN_UP = "signUp";
    public static final String CHANGE_PW = "changePw";
    public static final String OAUTH = "oauth";


    @Value("${uri.user-service}")
    private String userUri = "";

    public AuthController(RestClient restClient, JwtUtils jwtUtils, AuthService authService) {
        this.restClient = restClient;
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public CommonResponse signIn(@RequestBody HashMap<String, String> requestBody) {
        HashMap responseEntity;
        String response = restClient.restTemplatePost(userUri, "/auth/signIn", requestBody);
        return authService.parseResponseWrapper(response, SIGN_IN);
    }

    @PostMapping("/signUp")
    public CommonResponse signUp(@RequestBody HashMap<String, Object> requestBody) {
        HashMap responseEntity;
        String response = restClient.restTemplatePost(userUri, "/auth/signUp", requestBody);
        return authService.parseResponseWrapper(response, SIGN_UP);
    }

    @PostMapping("/changePW")
    public CommonResponse changePw(@RequestBody HashMap<String, Object> requestBody) {
        HashMap responseEntity;
        String response = restClient.restTemplatePost(userUri, "/auth/changePW", requestBody);
        return authService.parseResponseWrapper(response, CHANGE_PW);
    }


    @GetMapping("/oauth/{socialLoginType}")
    public CommonResponse accessOauth(@PathVariable("socialLoginType") String oauthType, @RequestParam("code") String code) {
        String response = restClient.restTemplateGet(userUri, String.format("/auth/oauth/%s?code=%s", oauthType, code),null);

        return authService.parseResponseWrapper(response, OAUTH);
    }
}
