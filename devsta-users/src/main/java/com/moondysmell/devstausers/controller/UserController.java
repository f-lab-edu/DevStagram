package com.moondysmell.devstausers.controller;

import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CommonResponse;
import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.UserDetailDto;
import com.moondysmell.devstausers.domain.dto.UserSummaryDto;
import com.moondysmell.devstausers.service.DevUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@AllArgsConstructor
@RestController
@Slf4j
//@RequestMapping("/profile")
public class UserController {
    private final DevUserService devUserService;

    @GetMapping("/whoAmI")
    public CommonResponse whoamI(@RequestHeader("userId") String userId) {
        DevUser me = devUserService.findUserById(userId);
        HashMap attribute = new HashMap();
        attribute.put("me", new UserSummaryDto(me));
        return new CommonResponse(CommonCode.SUCCESS, attribute);
    }

    @PutMapping("/update")
    public CommonResponse updateProfile(@RequestHeader("userId") String userId,
                                        @RequestBody UserDetailDto userDetailDto) {
        devUserService.updateProfile(userId, userDetailDto);
        return new CommonResponse(CommonCode.SUCCESS);
    }

}
