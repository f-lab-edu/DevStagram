package com.moondysmell.devstausers.domain.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserJoinDto {


    private String name;
    private String nickname;
    private String email;
    private String password;
    private String pictureUrl;

    //홈페이지 로그인인지 oauth로그인인지 구분해주기 위해
    private String provider; //google, naver, kakao..
    private String providerId; //sub

    @Builder
    public UserJoinDto(String name, String nickname, String email, String password, String pictureUrl, String provider, String providerId){
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.pictureUrl = pictureUrl;
        this.provider = provider;
        this.providerId = providerId;
    }

}
