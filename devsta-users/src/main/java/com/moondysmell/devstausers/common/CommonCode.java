package com.moondysmell.devstausers.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;

@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum CommonCode {
    // SUCCESS
    SUCCESS(200, 200, "성공"),
    // FAIL
    FAIL(500, -1, "실패. 알 수 없는 오류"),

    //-1000: USER
    NOT_EXIST_ID(400,-1000, "존재하지 않는 이메일입니다."),
    WRONG_PASSWORD(400, -1001, "비밀번호가 일치하지 않습니다.");

    //-2000: MeetUp

    //-3000: Posts

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
