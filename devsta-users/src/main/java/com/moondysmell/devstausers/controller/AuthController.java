package com.moondysmell.devstausers.controller;

import com.mongodb.client.result.UpdateResult;
import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CommonResponse;
import com.moondysmell.devstausers.common.CustomException;
import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.ChangePwDto;
import com.moondysmell.devstausers.domain.dto.LoginDto;
import com.moondysmell.devstausers.service.DevUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final DevUserService devUserService;
    @GetMapping("/getAll")
    public List<DevUser> getAllUsers() {
        return devUserService.findAll();
    }

    @GetMapping("/getByEmail")
    public DevUser getByEmail(@RequestParam String email) {
        return devUserService.findUserByEmail(email);
    }

    @GetMapping("/signIn")
    public CommonResponse signIn(@RequestBody LoginDto requestBody) {
        devUserService.checkExistUser(requestBody.getEmail(), requestBody.getPassword());
        return new CommonResponse(CommonCode.SUCCESS);
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



}
