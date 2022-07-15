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

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final DevUserService devUserService;

    @GetMapping("/whoAmI")
    public CommonResponse whoamI(@RequestHeader("userId") String userId) {
        DevUser me = devUserService.findUserById(userId);
        HashMap attribute = new HashMap();
        attribute.put("me", new UserSummaryDto(me));
        return new CommonResponse(CommonCode.SUCCESS, attribute);
    }


    @GetMapping("/join")
    public String join() {
        return "join";
        //회원가입페이지로 이동
    }

    //회원가입 후 submit할떄 맵핑되는 메소드
    //dto에 회원정보 저장하고, Service에 전달
    //@PostMapping("/joinProc")
    @RequestMapping(value = "/joinProc", method =RequestMethod.POST)
    public @ResponseBody String joinProc(String name, String nickname, String password, String email) {
        //String rawPassword = pw;
        //String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setName(name);
        userJoinDto.setNickname(nickname);
        userJoinDto.setPassword(password);
        userJoinDto.setEmail(email);
        devUserService.join(userJoinDto);
        return "redirect:/user/login";
    }
}
