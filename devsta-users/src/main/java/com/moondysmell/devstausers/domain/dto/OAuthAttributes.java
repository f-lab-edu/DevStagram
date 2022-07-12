package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Getter
public class OAuthAttributes implements UserDetails, OAuth2User {
//Authentication 객체에 저장할 수 있는 유일한 타입
//일반로그인, oauth로그인할때 객체로 묶어서 처리하기 위한 클래스

    private static final long serialVersionUID = 1L;
    private Map<String, Object> attributes;
    private DevUser devUser;
    //private String nameAttributeKey;
    //private String name;
    //private String email;
    //private String picture;

    //일반 로그인시 사용
    public OAuthAttributes(DevUser devUser){
        this.devUser = devUser;
    }

    //Oauth2.0 로그인 시 사용
    public OAuthAttributes(Map<String, Object> attributes,
                           DevUser devUser) {
        this.devUser = devUser;
        this.attributes = attributes;
    }

    // Map을 받아서
//    public static OAuthAttributes of(String registrationId,
//        String userNameAttributeName,
//        Map<String, Object> attributes) {
//        return ofGoogle(userNameAttributeName, attributes);
//    }

    //OAuthAttributes로 변환
//    private static OAuthAttributes ofGoogle(String userNameAttributeName,
//        Map<String, Object> attributes) {
//        return OAuthAttributes.builder()
//                   .name((String) attributes.get("name"))
//                   .email((String) attributes.get("email"))
//                   .picture((String) attributes.get("picture"))
//                   .attributes(attributes)
//                   .nameAttributeKey(userNameAttributeName)
//                   .build();
//    }

//    public UserInitSaveDto convert() {
//        return UserInitSaveDto.builder()
//                .name(name)
//                .email(email)
//                .pictureUrl(picture)
//                .createdDt(new Date())
//                .build();
//    }

    //DevUser Entity로 변환
//    public DevUser toEntity() {
//        return DevUser.builder()
//                .id(nameAttributeKey)
//                .name(name)
//                .nickname(null)
//                .password(null)
//                .pictureUrl(picture)
//                .createdDt(new Date())
//                .description(null)
//                .email(email)
//                .github(null)
//                .blog(null)
//                .tags(null)
//                .build();
//    }

    //해당 User의 권한을 return하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<GrantedAuthority>();
        collet.add(()->{ return Role.USER.getKey();});
        return collet;
    }

    public DevUser getUser() {
        return devUser;
    }

    @Override
    public String getPassword() {
        return devUser.getPassword();
    }

    @Override
    public String getUsername() {
        return devUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 리소스 서버로 부터 받는 회원정보
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // User의 PrimaryKey(필요없음)
    @Override
    public String getName() {
        return devUser.getId()+"";
    }
}

