package com.moondysmell.devstausers.controller;

import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.UserInitSaveDto;
import com.moondysmell.devstausers.domain.dto.UserJoinDto;
import com.moondysmell.devstausers.repository.DevUserRepository;
import com.moondysmell.devstausers.service.DevUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final DevUserService devUserService;
    private final HttpSession httpSession;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/getAll")
    public List<DevUser> getAllUsers() {
        return devUserService.findAll();
    }
    @GetMapping("/getByEmail")
    public DevUser getByEmail(@RequestParam String email) {
        return devUserService.findUserByEmail(email);
    }

    @GetMapping({ "", "/" })
    public @ResponseBody String index() {
//        UserInitSaveDto user = (UserInitSaveDto) httpSession.getAttribute("user");
//        if(user != null){
//            model.addAttribute("userName", user.getName());
//        }

        return "인덱스 페이지";
    }

    @GetMapping("/test/oauth")
    public @ResponseBody String testLogin(Authentication authentication
                        , @AuthenticationPrincipal OAuth2User oAuth ) {
        System.out.println("/test/login==============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oAuth.getAttributes());

        return "세션정보 확인" + oAuth2User.getAttributes();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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

    @PostMapping(value = "/users/summary/in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummaries(@RequestBody List<String> usernames) {
        log.info("retrieving summaries for {} usernames", usernames.size());

        List<UserSummary> summaries =
                devUserService
                        .findByUsernameIn(usernames)
                        .stream()
                        .map(user -> convertTo(user))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(summaries);

    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUserProfile().getDisplayName())
                .profilePicture(user.getUserProfile().getProfilePictureUrl())
                .build();
    }
}
