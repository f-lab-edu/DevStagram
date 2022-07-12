package com.moondysmell.devstaposts.controller;


import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.document.User;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PostsSaveRequestDto postsSaveRequestDto, User user){

        Posts posts = postsService.post(postsSaveRequestDto, user);
        return ResponseEntity.ok(posts);
    }


}
