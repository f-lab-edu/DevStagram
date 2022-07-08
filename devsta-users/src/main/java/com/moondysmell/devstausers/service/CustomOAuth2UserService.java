package com.moondysmell.devstausers.service;


import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.OAuthAttributes;
import com.moondysmell.devstausers.domain.dto.UserInitSaveDto;
import com.moondysmell.devstausers.domain.dto.UserJoinDto;
import com.moondysmell.devstausers.domain.enums.Role;
import com.moondysmell.devstausers.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private final DevUserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final HttpSession httpSession;

    final static private String COLLECTION_NAME="DevUser";
    private final DevUserService devUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    //구글로 부터 받은 액세스 토큰정보가 userRequest 데이터 return 되고 에 대한 후처리가 되는 함
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // code를 통해 구성한 정보
        log.info("userRequest clientRegistration : " + userRequest.getClientRegistration()); //어떤 oauth로 로그인했는지 확인 가능
        // token을 통해 응답받은 회원정보
        log.info("oAuth2User : " + userRequest.getAccessToken().getTokenValue());
        //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인완료 -> code리턴(oauth-client라이브러리) -> access token요청
        //userRequest 정보 get -> loadUser함수를 통해 구글로부터회원프로필 get
        log.info("getAttributes : " + oAuth2User.getAttributes());


        //회원가입
        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String picture = oAuth2User.getAttribute("picture");
        String userName = oAuth2User.getAttribute("name"); //oauth면 필요없음
        String nickname = oAuth2User.getAttribute("given_name");
        String password = "1234"; //oauth면 필요없음
        System.out.println(provider);

        DevUser userEntity = devUserService.findUserByEmail(email);
        if(userEntity == null){
            userEntity = DevUser.builder()
                    .name(userName)
                    .nickname(nickname)
                    .email(email)
                    .password(password)
                    .pictureUrl(picture)
                   // .provider(provider)
                  //  .providerId(providerId)
                    .build();
            mongoTemplate.insert(userEntity,COLLECTION_NAME);
        }
        return new OAuthAttributes(oAuth2User.getAttributes(), userEntity);


        //현재 로그인 진행중인 서비스를 구분하는 코드. 지금은 구글만이지만 나중에 다른 OAuth 서비스 추가할 때 유용
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        // OAuth2로그인 진행시 키가 되는 필드값
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//        //OAuth2User의 attributes를 담을 클래스
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        DevUser user = saveOrUpdate(attributes);
//
//        httpSession.setAttribute("user", new UserInitSaveDto(user));
//
//        //만일을 위해 권한을 나눠놨는데, 현재는 특별히 정책 없어서 무조건 USER로 생성
//        return new DefaultOAuth2User(
//            Collections.singleton(new SimpleGrantedAuthority(Role.USER.getKey())),
//            attributes.getAttributes(),
//            attributes.getNameAttributeKey());
    }

    // 구글 사용자 정보가 변경되었을 때 User 엔티티에도 반영
//    private DevUser saveOrUpdate(OAuthAttributes attributes) {
//        DevUser user = devUserService.findUserByEmail(attributes.getEmail());
//        DevUser check = (user == null)? attributes.toEntity() : user.update(attributes.getName(), attributes.getPicture());
//        return userRepository.save(user);
//    }

}