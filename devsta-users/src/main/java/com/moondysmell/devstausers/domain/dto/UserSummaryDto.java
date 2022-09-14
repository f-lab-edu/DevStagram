package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.Data;
import lombok.Getter;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Getter
public class UserSummaryDto implements Serializable {
    private String id;
    private String name;
    private String nickname;
    private String pictureUrl;
    private Date createdDt;
    private String description;
    private String email;
    private String github;
    private String blog;
    private List<String> tags;
    private String provider;

    public UserSummaryDto(DevUser devUser) {
        this.id = devUser.getId().toString();
        this.name = devUser.getName();
        this.nickname = devUser.getNickname();
        this.pictureUrl = devUser.getPictureUrl();
        this.createdDt = devUser.getCreatedDt();
        this.description = devUser.getDescription();
        this.email = devUser.getEmail();
        this.github = devUser.getGithub();
        this.blog = devUser.getBlog();
        this.tags = devUser.getTags();
    }
}
