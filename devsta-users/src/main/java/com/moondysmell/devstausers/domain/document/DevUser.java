package com.moondysmell.devstausers.domain.document;

import com.moondysmell.devstausers.domain.dto.UserDetailDto;
import com.moondysmell.devstausers.domain.dto.UserJoinDto;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "DevUser")
@Data
//@RequiredArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DevUser {

    @Transient
    public static final String SEQUENCE_NAME = "devuser_sequence";

    @Id
    @Field("_id")
    private ObjectId id;
    private String name;
    private String nickname;
    private String password;
    @Field("picture_url")
    private String pictureUrl;
    @Field("created_dt")
    private Date createdDt;
    private String description;
    private String email;
    private String github;
    private String blog;
    private List<String> tags;

    //Oauth를 위해 구성한 추가 필드
    private String provider;

    public DevUser update(String nickname, String picture) {
        this.nickname = nickname;
        this.pictureUrl = picture;
        return this;
    }

    public DevUser ofDetail(UserDetailDto userDetailDto) {
        this.nickname = userDetailDto.getNickname();
        this.password = userDetailDto.getPassword();
        this.pictureUrl = userDetailDto.getPictureUrl();
        this.createdDt = new Date();
        this.description = userDetailDto.getDescription();
        this.email = userDetailDto.getEmail();
        this.github = userDetailDto.getGithub();
        this.blog = userDetailDto.getBlog();
        this.tags = userDetailDto.getTags();
        this.provider = "app";
        return this;
    }

}