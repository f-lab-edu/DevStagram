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
import com.moondysmell.devstausers.service.DevUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    private final DevUserService devUserService;
    private final String DEFAULT_PICTURE_URL = "https://toppng.com//public/uploads/preview/user-account-management-logo-user-icon-11562867145a56rus2zwu.png";

    @GetMapping("/signIn")
    public CommonResponse signIn(@RequestBody LoginDto requestBody) {
        DevUser user = devUserService.checkExistUser(requestBody.getEmail(), requestBody.getPassword());
        HashMap<String, DevUser> attribute = new HashMap<>();
        attribute.put("devUser", user);
        return new CommonResponse<DevUser>(CommonCode.SUCCESS, attribute);
    }

    @PostMapping("/changePW")
    public CommonResponse changePW(@RequestBody ChangePwDto requestBody) {
        devUserService.checkExistUser(requestBody.getEmail(), requestBody.getPassword());
        UpdateResult result = devUserService.updatePw(requestBody);
        if (result.getModifiedCount() == 0 ) {
            return new CommonResponse(CommonCode.FAIL);
        }
        return new CommonResponse(CommonCode.SUCCESS);
    }

    @PostMapping("/signUp")
    public CommonResponse signUp(@RequestBody UserDetailDto userDetailDto) {
        if (devUserService.findAllUserByEmail(userDetailDto.getEmail()).size() > 0) throw new CustomException(CommonCode.USER_ALREADY_EXIST);
        if (devUserService.findAllUserByNickname(userDetailDto.getNickname()).size() >0) throw new CustomException(CommonCode.NICKNAME_ALREADY_EXIT);
        if (userDetailDto.getPictureUrl() == null) {
            userDetailDto.setPictureUrl(DEFAULT_PICTURE_URL);
        }
        try {
            DevUser savedUser = devUserService.saveDetail(userDetailDto);
            HashMap attribute = new HashMap();
            attribute.put("userId", savedUser.getId().toString());
            attribute.put("email", savedUser.getEmail());
            if (savedUser != null) return new CommonResponse(CommonCode.SUCCESS, attribute);
            return new CommonResponse(CommonCode.FAIL);

        }catch (Exception e) {
            log.error(">>> " + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }


    }





}
