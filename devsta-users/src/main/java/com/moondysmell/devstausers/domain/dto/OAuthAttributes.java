package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // Map을 받아서
    public static OAuthAttributes of(String registrationId,
        String userNameAttributeName,
        Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    //OAuthAttributes로 변환
    private static OAuthAttributes ofGoogle(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                   .name((String) attributes.get("name"))
                   .email((String) attributes.get("email"))
                   .picture((String) attributes.get("picture"))
                   .attributes(attributes)
                   .nameAttributeKey(userNameAttributeName)
                   .build();
    }

    public UserInitSaveDto convert() {
        return UserInitSaveDto.builder()
                .name(name)
                .email(email)
                .pictureUrl(picture)
                .createdDt(new Date())
                .build();
    }

    //DevUser Entity로 변환
    public DevUser toEntity() {
        return DevUser.builder()
                .id(nameAttributeKey)
                .name(name)
                .nickname(null)
                .password(null)
                .pictureUrl(picture)
                .createdDt(new Date())
                .description(null)
                .email(email)
                .github(null)
                .blog(null)
                .tags(null)
                .build();
    }
}

