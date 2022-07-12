package com.moondysmell.devstaposts.exception;

import lombok.Getter;

public class PostsException extends RuntimeException {

    @Getter
    private ExceptionType ExceptionType;

    public PostsException(ExceptionType ExceptionType){
        super(ExceptionType.getErrorMessage());
        this.ExceptionType = ExceptionType;
    }
}
