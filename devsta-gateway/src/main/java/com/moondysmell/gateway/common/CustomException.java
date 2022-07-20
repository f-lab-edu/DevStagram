package com.moondysmell.gateway.common;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private CommonCode commonCode;


    public CustomException(String message, CommonCode commonCode) {
        super(message);
        this.commonCode = commonCode;
    }

    public CustomException(CommonCode commonCode) {
        super(commonCode.getMessage());
        this.commonCode = commonCode;
    }


    public CommonCode getErrorCode() {
        return this.commonCode;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(this.commonCode.getStatus());
    }
}