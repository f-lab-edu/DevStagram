package com.moondysmell.devstaposts.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {
    //User
    NOT_FOUND_USER(1001, 200, "해당하는 사용자가 존재하지 않습니다."),

    //Posts
    NO_QUALIFICATION_USER(2001, 200, "권한없는 유저입니다."),
    NOT_MATCH_WRITER(2002, 200, "작성자만 수행가능한 작업입니다."),
    NOT_FOUNT_CONTENTS(2003, 200, "존재하지 않는 게시글입니다.");

    private int errorCode;
    private int httpStatus;
    private String errorMessage;

    ExceptionType(int errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
