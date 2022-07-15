package com.moondysmell.devstaposts.controller;


import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CommonResponse;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/create")
    public CommonResponse save(@RequestBody PostsSaveRequestDto postsSaveRequestDto){
        // 유저 세션이 없을때 예외처리 (추후)
//            throw new 로그인한 유저의 정보가 없습니다.

        try{
            Posts savePosts = postsService.save(postsSaveRequestDto);
            return new CommonResponse(CommonCode.SUCCESS, Map.of("savePosts", savePosts));
        }catch (Exception e){
            log.error(">>>" + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }

    }

    @GetMapping("/timeline")
    public CommonResponse<List<Posts>> viewAll(){

        try{
            List<Posts> posts = postsService.viewAll();
            return new CommonResponse(CommonCode.SUCCESS, Map.of("posts", posts));
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


}
