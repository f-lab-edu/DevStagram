package com.moondysmell.devstausers.domain.dto;

import com.moondysmell.devstausers.domain.document.DevUser;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@RequiredArgsConstructor
public class UserDetailDto implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String nickname;
    @NotBlank
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;
    @Field("picture_url")
    private String pictureUrl;
    @Field("created_dt")
    private Date createdDt = null;
    private String description;
    @Email
    @NotBlank
    private String email;
    private String github;
    private String blog;
    private List<String> tags;

    //Oauth를 위해 구성한 추가 필드
    @Field("provider")
    private String provider;


}
