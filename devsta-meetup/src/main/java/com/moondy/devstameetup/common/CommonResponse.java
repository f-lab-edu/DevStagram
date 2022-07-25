package com.moondy.devstameetup.common;

import lombok.Data;

import java.util.Map;

@Data
public class CommonResponse<T> {
    private int code;
    private String message;
    private Map<String, T> attribute;

    public CommonResponse(CommonCode commonCode, Map<String, T> attribute) {
        this.code = commonCode.getCode();
        this.message = commonCode.getMessage();
        this.attribute = attribute;
    }

    public CommonResponse(CommonCode commonCode) {
        this.code = commonCode.getCode();
        this.message = commonCode.getMessage();
    }

}
