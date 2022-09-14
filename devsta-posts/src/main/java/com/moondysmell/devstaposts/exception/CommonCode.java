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
    INVALID_ELEMENTS(400, -2, "조건에 맞지 않는 요소(elements)가 있습니다"),

    //-1000: USER

    //-2000: MeetUp

    //-3000: Posts
    CONTENT_IS_MANDATORY(400,-3000, "내용이 존재하지 않습니다."),
    NO_QUALIFICATION_USER(400,-3001, "권한이 없습니다."),
    NOT_MATCH_WRITER(400, -3002, "작성자만 수행가능한 작업입니다."),
    NOT_FOUNT_CONTENTS(400, -3003, "존재하지 않는 게시글입니다."),
    POST_UPDATE_FAIL(400, -3004, "게시글 수정 실패"),
    NOT_FOUNT_COMMENTS(400, -3005, "존재하지 않는 댓글입니다."),
    COMMENT_UPDATE_FAIL(400, -3006, "댓글 수정 실패"),
    COMMENT_NO_CHANGE(400, -3007, "변경사항이 없습니다."),
    ALREADY_LIKE(400, -3008, "이미 좋아요를 누른 유저입니다.");

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
