package com.moondysmell.devstaposts.controller;


import com.moondysmell.devstaposts.config.PostsAssembler;
import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CommonResponse;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.service.PostsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
//@RequestMapping("/posts")
public class PostsController {

    private final PostsAssembler postsAssembler;
    private final PostsService postsService;

    @Value("${url.gateway}")
    private String GATEWAY_URL = "";

    public PostsController(PostsService postsService, PostsAssembler postsAssembler) {
        this.postsService = postsService;
        this.postsAssembler = postsAssembler;
    }


    //피드 생성
    @PostMapping("/create")
    public CommonResponse save(@RequestBody PostsSaveRequestDto postsSaveRequestDto, @RequestHeader String userId){
        // 유저 세션이 없을때 예외처리 (추후)
        // throw new 로그인한 유저의 정보가 없습니다.

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
    public CollectionModel<EntityModel<Posts>> viewAll(@RequestParam int page, @RequestParam int size){

        try{
            List<Posts> posts = postsService.viewAll(page, size);
            return postsAssembler.toCollection(posts,Link.of(String.format("%s/timeline?page=%d&size=%d", GATEWAY_URL, page + 1, size), "next"));
            //return new CommonResponse(CommonCode.SUCCESS, Map.of("posts", posts));
        }catch (Exception e){
            log.error(">>>" + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }

    }

    //내 피드만 조회(프로필)
    @GetMapping("/myFeed")
    public CollectionModel<EntityModel<Posts>> viewMyFeed(@RequestHeader("userId") String userId, @RequestParam int page, @RequestParam int size){

        try{
            List<Posts> posts = postsService.findAllById(userId, page, size);

            return postsAssembler.toCollection(posts,Link.of(String.format("%s/myFeed?page=%d&size=%d", GATEWAY_URL, page + 1, size), "next"));
        }catch (Exception e){
            log.error(">>>" + e.getMessage());
            throw new CustomException(CommonCode.FAIL);
        }

    }

    @GetMapping("/getOneFeed")
    public CommonResponse getOneFeed(@RequestParam String id){

        try{
            Posts posts = postsService.getOneFeed(id);
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
