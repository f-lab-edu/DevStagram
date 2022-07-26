package com.moondysmell.devstaposts.controller;

import com.moondysmell.devstaposts.domain.document.Comments;
import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.CommentDto;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CommonResponse;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/comments")
public class CommnetsController {

    private final CommentService commentService;

    public CommnetsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public CommonResponse createComment(@RequestBody @Valid CommentDto commentDto, @RequestHeader String userId) {

        String contents = commentDto.getContents();

        if (contents.isEmpty()) throw new CustomException(CommonCode.CONTENT_IS_MANDATORY);
        Comments comments = commentService.createComment(commentDto, userId);

        return new CommonResponse(CommonCode.SUCCESS, Map.of("createComment", comments));

    }

    @GetMapping("/list/{postId}")
    public CommonResponse<List<Comments>> viewCommentsList(@PathVariable("postId") Long postId) {
        if (postId == null) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);
        List<Comments> comments = commentService.getComments(postId);
        HashMap attribute = new HashMap();
        attribute.put("commentCount", comments.size());
        attribute.put("CommentsList", comments);
        return new CommonResponse<>(CommonCode.SUCCESS, attribute);

    }

    //댓글 수정
    @PutMapping("/update/{commentId}")
    public CommonResponse updateComments(@PathVariable Long commentId, @RequestHeader("userId") String userId, @RequestBody CommentDto commentDto) {

        //콘텐츠 없을시 예외처리
        String contents = commentDto.getContents();
        if (contents.isEmpty()) throw new CustomException(CommonCode.CONTENT_IS_MANDATORY);

        Comments comments = commentService.updateComments(commentId, userId, commentDto);

        return new CommonResponse(CommonCode.SUCCESS, Map.of("updateComments", comments));

    }

    @GetMapping("/delete/{commentId}/posts/{postId}")
    public CommonResponse deleteComment(@PathVariable Long commentId, @PathVariable Long postId, @RequestHeader String userId) {

        commentService.deleteComment(commentId, userId);

        HashMap attribute = new HashMap();
        attribute.put("postId", postId);
        attribute.put("deleteCommentId", commentId);
        return new CommonResponse(CommonCode.SUCCESS, attribute);


    }


}
