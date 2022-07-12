package com.moondysmell.devstaposts.domain.dto;

import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.document.User;
import lombok.*;




@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String contents;

    @Builder
    public PostsSaveRequestDto(String contents){
        this.contents = contents;
    }

    public Posts convertPosts(User user){
        return Posts.builder()
                .user(user)
                .contents(this.contents)
                .build();
    }

}
