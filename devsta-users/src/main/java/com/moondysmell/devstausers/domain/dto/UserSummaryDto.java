package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.Data;
import lombok.Getter;


import java.io.Serializable;


@Data
@Getter
public class UserSummaryDto implements Serializable {
    private String nickname;
    private String pictureUrl;
    private String email;

    public UserSummaryDto(DevUser devUser) {
        this.nickname = devUser.getNickname();
        this.email = devUser.getEmail();
        this.pictureUrl = devUser.getPictureUrl();
    }
}
