package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


import java.io.Serializable;
import java.util.Date;


@Data
@Getter
public class UserInitSaveDto implements Serializable {
    private String name;

    private String pictureUrl;
    private String email;

    public UserInitSaveDto(DevUser devUser) {
        this.name = devUser.getName();
        this.email = devUser.getEmail();
        this.pictureUrl = devUser.getPictureUrl();
    }
}
