package com.moondysmell.devstausers.service;

import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CustomException;
import com.moondysmell.devstausers.domain.oauth.GetSocialOAuthRes;
import com.moondysmell.devstausers.domain.oauth.GoogleOAuthToken;
import com.moondysmell.devstausers.domain.oauth.GoogleOauth;
import com.moondysmell.devstausers.domain.oauth.GoogleUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final GoogleOauth googleOauth;

    public GetSocialOAuthRes oAuthLogin(String socialLoginType, String code) throws CustomException {
        GetSocialOAuthRes result;
        switch (socialLoginType) {
            case "GOOGLE": {
                try {
                    //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
//                    GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(code);
                    //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                    ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(code);
                    //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                    GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);
                    log.info("googleUser: " + googleUser.getEmail());
                    result = new GetSocialOAuthRes(googleUser.email, googleUser.name, googleUser.getPicture());
                    break;
                } catch (Exception e) {
                    log.error(">>>" + e.getMessage());
                    throw new CustomException(CommonCode.OAUTH_LOGIN_FAILED);
                }

            }
            default: {
                throw new CustomException(CommonCode.INVALID_SOCIAL_LOGIN_TYPE);
            }
        }
        return result;
    }
}