package com.moondysmell.gateway.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class TokenUser {
    private String id;
    private String email;
}