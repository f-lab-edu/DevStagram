package com.moondysmell.devstaposts.controller;


import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CommonResponse;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.service.PostsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }


    //피드 생성
    @PostMapping("/create")
    public CommonResponse save(@RequestBody PostsSaveRequestDto postsSaveRequestDto, @RequestHeader String userId){
        // 유저 세션이 없을때 예외처리 (추후)
//            throw new 로그인한 유저의 정보가 없습니다.

        try{
            String contents = postsSaveRequestDto.getContents();
            if(contents.isEmpty()) throw new CustomException(CommonCode.CONTENT_IS_MANDATORY);

            Posts savePosts = postsService.savePost(postsSaveRequestDto, userId);
            return new CommonResponse(CommonCode.SUCCESS, Map.of("savePosts", savePosts));
        }catch (Exception e){
            log.error(">>>" + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }

    }

    //모든 피드 조회(타임라인) - 최신순
    @GetMapping("/timeline")
    public CommonResponse viewAll(){

        try{
            List<Posts> posts = postsService.viewAll();
            return new CommonResponse(CommonCode.SUCCESS, Map.of("posts", posts));
        }catch (Exception e){
            log.error(">>>" + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }

    }

    //내 피드만 조회(프로필)
    @GetMapping("/myFeed")
    public CommonResponse<List<Posts>> viewMyFeed(@RequestHeader("userId") String userId){

        try{
            List<Posts> posts = postsService.findAllById(userId);
            //if(posts.isEmpty()) throw new CustomException(CommonCode.NOTHING_IN_MY_FEED);
            return new CommonResponse(CommonCode.SUCCESS, Map.of("myFeedList", posts));
        }catch (Exception e){
            log.error(">>>" + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }

    }

//    @PostMapping("/follower/timeline")
//    public CommonResponse<?> findPostsByIdIn(@RequestBody List<String> ids) {
//
//        try{
//            List<Posts> posts = postsService.viewAll(ids);
//            log.info("found {} posts", posts.size());
//
//            return new CommonResponse(CommonCode.SUCCESS, Map.of("posts", posts));
//        }catch (Exception e){
//            log.error(">>>" + e.getMessage());
//            throw new CustomException(CommonCode.FAIL);
//        }
////   }


    //피드 수정
    @PutMapping("/update/{postId}")
    public CommonResponse updatePost(@PathVariable String postId,  @RequestHeader("userId") String userId, @RequestBody PostsSaveRequestDto saveRequestDto){
            Posts posts = postsService.update(postId, userId, saveRequestDto);
            return new CommonResponse(CommonCode.SUCCESS, Map.of("update", posts));

    }

    //피드 삭제
    @GetMapping("/delete/{postId}")
    public CommonResponse deletePost(@PathVariable String postId,  @RequestHeader("userId") String userId){
        postsService.delete(postId, userId);
        return new CommonResponse(CommonCode.SUCCESS);
    }

    //좋아요 클릭
    @PostMapping("/like/{postId}")
    public CommonResponse like(@PathVariable Long postId, @RequestHeader String userId){
        int heartCount = postsService.like(postId, userId);

        HashMap attribute = new HashMap();
        attribute.put("heartCount", heartCount);
        return new CommonResponse(CommonCode.SUCCESS, attribute);
    }

}
