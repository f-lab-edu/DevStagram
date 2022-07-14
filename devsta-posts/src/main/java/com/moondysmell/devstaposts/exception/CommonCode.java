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
    CONTENT_IS_MANDATORY(400,-3000, "내용이 존재하지 않습니다.");

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
