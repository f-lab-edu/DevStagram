package com.moondysmell.devstausers.service;


import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.OAuthAttributes;
import com.moondysmell.devstausers.domain.enums.Role;
import com.moondysmell.devstausers.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final DevUserRepository userRepository;
    private final HttpSession httpSession;
    private final DevUserService devUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //현재 로그인 진행중인 서비스를 구분하는 코드. 지금은 구글만이지만 나중에 다른 OAuth 서비스 추가할 때 유용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2로그인 진행시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        //OAuth2User의 attributes를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        DevUser user = saveOrUpdate(attributes);

//        httpSession.setAttribute("user", new SessionUser(user));
        //만일을 위해 권한을 나눠놨는데, 현재는 특별히 정책 없어서 무조건 USER로 생성
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(Role.USER.getKey())),
            attributes.getAttributes(),
            attributes.getNameAttributeKey());
    }

    // 구글 사용자 정보가 변경되었을 때 User 엔티티에도 반영
    private DevUser saveOrUpdate(OAuthAttributes attributes) {
        DevUser user = devUserService.findUserByEmail(attributes.getEmail());
        DevUser check = (user == null)? attributes.toEntity() : user.update(attributes.getName(), attributes.getPicture());
        return userRepository.save(user);
    }
}