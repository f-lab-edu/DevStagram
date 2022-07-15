package com.moondysmell.devstausers.domain.dto;

import lombok.Getter;

@Getter
public class ChangePwDto {
    private String email;
    private String password;
    private String newPassword;
}
