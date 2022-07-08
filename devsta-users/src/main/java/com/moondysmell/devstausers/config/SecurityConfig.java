package com.moondysmell.devstausers.config;


import com.moondysmell.devstausers.domain.enums.Role;
import com.moondysmell.devstausers.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity //스프링 시큐티리 필터가 스프링 필터체인에 등록이된다.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean //해당 메소드의 리턴되는 오브젝트를 loc로 등록해준다.
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private final CustomOAuth2UserService customOAuth2UserService;

    //WebSecurityConfigurerAdapter 를 상속받아 사용할 때. deprecated 되어서 위의 코드로 수정
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //1. 코드받기(인증)
        // 2.액세스토큰(권한)
        // 3. 사용자 프로필정보 가져오기
        // 4.그 정보를 토대로 회원가입진행
        // 4-2. (이메일, 전화번호, 이름, 아이디) + (닉네임) 추가 디테일 필요
        http.csrf().disable();
        http.headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //권한
                .anyRequest().permitAll() //anymatcher 제외한 모든권한
                .and()
                    .formLogin() //권한이 없는 페이지들어갈때 로그인 페이지가 뜨도록
                    .loginPage("/user/login")
                    .loginProcessingUrl("/user/loginProc")//해당 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                    .defaultSuccessUrl("/user")// 로그인 완료시 메인페이지 이동
                .and()
                    .oauth2Login()
                    .loginPage("/user/login") //구글 로그인 완료 된 후 처리
                    .defaultSuccessUrl("/user")
                    .userInfoEndpoint()// 구글 로그인 완료되면 코드x (액세스토큰 + 사용자 프로필정보)
                    .userService(customOAuth2UserService);

    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .headers().frameOptions().disable()
//            .and()
//            .authorizeRequests()
//            .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
//            .antMatchers("/user/**").permitAll()
//            .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // 추후 Role별로 나누고 싶어질 때 활용
//            .anyRequest().authenticated()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/")
//            .and()
//            .oauth2Login()
//            .userInfoEndpoint()
//            .userService(customOAuth2UserService);
//    }
}