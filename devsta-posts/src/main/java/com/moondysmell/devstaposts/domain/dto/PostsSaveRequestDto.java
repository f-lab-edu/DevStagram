package com.moondysmell.devstaposts.domain.dto;

import com.moondysmell.devstaposts.domain.document.Posts;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
public class PostsSaveRequestDto {

    private String userId;
    private String contents;
    private String pictureUrl;


//
////    public Posts convertPosts(User user){
////        return Posts.builder()
////                .user(user)
////                .contents(this.contents)
////                .build();
////    }

    public Posts toEntity(){
        return Posts.builder()
                .userId(userId)
                .contents(contents)
                .pictureUrl(pictureUrl)
                .build();
    }

}
