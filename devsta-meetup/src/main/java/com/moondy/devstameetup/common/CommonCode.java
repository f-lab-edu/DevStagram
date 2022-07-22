package com.moondy.devstameetup.common;

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

    //-2000: MeetUp
    CATEGORY_VALUE_NOT_EXIST(400, -2000, "존재하지 않는 카테고리 입니다."),
    MEETUP_NOT_EXIST(400, -2001, "존재하지 않는 밋업입니다."),
    UPDATE_FAILED(500, -2002, "수정에 실패했습니다."),
    DELETE_FAILED(500, -2004, "삭제에 실패했습니다."),
    NO_PERMISSION(400, -2005, "권한이 없습니다.");
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
