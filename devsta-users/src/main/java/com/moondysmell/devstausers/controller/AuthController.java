package com.moondysmell.devstausers.controller;

import com.mongodb.client.result.UpdateResult;
import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CommonResponse;
import com.moondysmell.devstausers.common.CustomException;
import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.ChangePwDto;
import com.moondysmell.devstausers.domain.dto.LoginDto;
import com.moondysmell.devstausers.domain.dto.UserDetailDto;
import com.moondysmell.devstausers.domain.dto.UserSummaryDto;
import com.moondysmell.devstausers.domain.oauth.GetSocialOAuthRes;
import com.moondysmell.devstausers.service.DevUserService;
import com.moondysmell.devstausers.service.OAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    private final DevUserService devUserService;
    private final OAuthService oAuthService;
    private final String DEFAULT_PICTURE_URL = "https://toppng.com//public/uploads/preview/user-account-management-logo-user-icon-11562867145a56rus2zwu.png";

    @PostMapping("/signIn")
    public CommonResponse signIn(@RequestBody @Valid LoginDto requestBody) {
        DevUser user = devUserService.checkExistUser(requestBody.getEmail(), requestBody.getPassword());
        //pasing 하기 쉽게 HashMap 사용(depth 최대한 얕게)
        HashMap<String, String> attribute = new HashMap<>();
        attribute.put("id", user.getId().toString());
        attribute.put("nickname", user.getNickname().toString());
        attribute.put("email", user.getEmail());
        return new CommonResponse(CommonCode.SUCCESS, attribute);
    }

    @GetMapping("/checkEmail")
    public CommonResponse checkEmail(@RequestParam("email") String email) {
        DevUser user = devUserService.findUserByEmail(email);
        return new CommonResponse(CommonCode.SUCCESS, Map.of("isExist", user != null));
    }
    @GetMapping("/oauth/{socialLoginType}")
    public CommonResponse accessOauth(@PathVariable("socialLoginType") String oauthType, @RequestParam("code") String code) {
        GetSocialOAuthRes res = oAuthService.oAuthLogin(oauthType.toUpperCase(), code);
        DevUser user = devUserService.findUserByEmail(res.getEmail());
        if (user == null) {
            return new CommonResponse(CommonCode.OAUTH_CHECK_SUCCESS, Map.of("userInfo", res));
        } else {
            // 유저가 이미 존재하는 경우 어떻게 Gateway에 데이터를 넘겨줄지에 따라 attribute 객체가 수정될 수 있음
            return new CommonResponse(CommonCode.USER_ALREADY_EXIST, Map.of("userInfo", new GetSocialOAuthRes(user)));
        }
    }

    @PostMapping("/changePW")
    public CommonResponse changePW(@RequestBody @Valid ChangePwDto requestBody) {
        devUserService.checkExistUser(requestBody.getEmail(), requestBody.getPassword());
        UpdateResult result = devUserService.updatePw(requestBody);
        if (result.getModifiedCount() == 0 ) {
            return new CommonResponse(CommonCode.FAIL);
        }
        return new CommonResponse(CommonCode.SUCCESS);
    }

    @PostMapping("/signUp")
    public CommonResponse signUp(@RequestBody @Valid UserDetailDto userDetailDto) {
        if (devUserService.findAllUserByEmail(userDetailDto.getEmail()).size() > 0) throw new CustomException(CommonCode.USER_ALREADY_EXIST);
        if (devUserService.findAllUserByNickname(userDetailDto.getNickname()).size() >0) throw new CustomException(CommonCode.NICKNAME_ALREADY_EXIST);
        if (userDetailDto.getPictureUrl() == null) {
            userDetailDto.setPictureUrl(DEFAULT_PICTURE_URL);
        }
        try {
            DevUser savedUser = devUserService.saveUser(userDetailDto);
            if (savedUser != null) return new CommonResponse(CommonCode.SUCCESS, Map.of("user", new UserSummaryDto(savedUser)));
            return new CommonResponse(CommonCode.FAIL);

        }catch (Exception e) {
            log.error(">>> " + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }


    }





}

