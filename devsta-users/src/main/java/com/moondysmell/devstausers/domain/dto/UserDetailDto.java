package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@RequiredArgsConstructor
public class UserDetailDto implements Serializable {
    @Id
    private String name;
    private String nickname;
    private String password;
    @Field("picture_url")
    private String pictureUrl;
    @Field("created_dt")
    private Date createdDt = null;
    private String description;
    private String email;
    private String github;
    private String blog;
    private List<String> tags;

    //Oauth를 위해 구성한 추가 필드
    @Field("provider")
    private String provider;


}
