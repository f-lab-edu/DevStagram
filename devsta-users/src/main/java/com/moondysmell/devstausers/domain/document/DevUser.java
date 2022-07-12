package com.moondysmell.devstausers.domain.document;

import lombok.*;
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
@Builder
public class DevUser {

    @Transient
    public static final String SEQUENCE_NAME = "devuser_sequence";

    @Id
    @Field("_id")
    private String id;
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
    private String providerId;

    public DevUser update(String name, String picture) {
        this.name = name;
        this.pictureUrl = picture;
        return this;
    }

}