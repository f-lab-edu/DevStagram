package com.moondysmell.devstausers.domain.oauth;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetSocialOAuthRes {
    String email;
    String userName;
    String pictureUrl;

    public GetSocialOAuthRes(DevUser user) {
        this.email = user.getEmail();
        this.userName = user.getName();
        this.pictureUrl = user.getPictureUrl();
    }
}