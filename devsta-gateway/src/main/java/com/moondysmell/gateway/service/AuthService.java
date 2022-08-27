package com.moondysmell.gateway.service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.moondysmell.gateway.auth.JwtUtils;
import com.moondysmell.gateway.auth.TokenUser;
import com.moondysmell.gateway.common.CommonCode;
import com.moondysmell.gateway.common.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.moondysmell.gateway.controller.AuthController.*;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final Gson gson = new Gson();
    private final JwtUtils jwtUtils;

    public CommonResponse parseResponseWrapper(String response, String uri) {
        HashMap responseEntity;
        try {
            responseEntity = gson.fromJson(response, HashMap.class);
            Double codeDouble = (Double) responseEntity.get("code");
            int code = codeDouble.intValue();
            switch (code) {
                case 200:
                    switch (uri) {
                        case SIGN_IN:
                        case OAUTH:
                            return parseSignInSuccess(responseEntity);
                        case SIGN_UP: return parseSignUpSuccess(responseEntity);
                        default: return parseChangePwSuccess(responseEntity);
                    }
                case 201:
                    log.info("oauthCheckSuccess >>> " + responseEntity);
                    return new CommonResponse(CommonCode.OAUTH_CHECK_SUCCESS, (LinkedTreeMap) responseEntity.get("attribute"));
                default:
                    return new CommonResponse(CommonCode.of((code)));
            }

        } catch (Exception e) {
            log.info(">>>  " + e);
            return new CommonResponse(CommonCode.FAIL, Map.of("message", e.getMessage()));
        }
    }

    private CommonResponse parseSignInSuccess(HashMap responseEntity) {
        LinkedTreeMap attribute = (LinkedTreeMap) responseEntity.get("attribute");
        String id = (String) attribute.get("id");
        String email = (String) attribute.get("email");
        String token = jwtUtils.generate(new TokenUser(id, email));
        log.info("parseSignInSuccess >>> ", token);
        return new CommonResponse(CommonCode.SUCCESS, Map.of("Authorization", token));
    }

    private CommonResponse parseSignUpSuccess(HashMap responseEntity) {
        LinkedTreeMap attribute = (LinkedTreeMap) responseEntity.get("attribute");
        return new CommonResponse(CommonCode.SUCCESS, attribute);
    }

    private CommonResponse parseChangePwSuccess(HashMap responseEntity) {
        return new CommonResponse(CommonCode.SUCCESS);
    }


}
