package com.moondy.devstameetup.common;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class CustomException extends RuntimeException{

    private CommonCode commonCode;

    // message는 log에 기록되고, 고객에게 return 되는 메세지는 commonCode에 따름
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