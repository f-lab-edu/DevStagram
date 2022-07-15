package com.moondysmell.devstaposts.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonCode {
    // SUCCESS
    SUCCESS(200, 200, "성공"),
    // FAIL
    FAIL(500, -1, "실패. 알 수 없는 오류"),

    //-1000: USER

    //-2000: MeetUp

    //-3000: Posts
    CONTENT_IS_MANDATORY(400,-3000, "내용이 존재하지 않습니다."),
    NO_QUALIFICATION_USER(400,-3001, "권한이 없습니다."),
    NOT_MATCH_WRITER(2002, 200, "작성자만 수행가능한 작업입니다."),
    NOT_FOUNT_CONTENTS(2003, 200, "존재하지 않는 게시글입니다.");

    //-4000: Gateway



    private int status;
    private int code;
    private String message;

    CommonCode(int status, int code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
