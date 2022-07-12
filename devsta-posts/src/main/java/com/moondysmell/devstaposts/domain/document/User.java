package com.moondysmell.devstaposts.domain.document;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@NoArgsConstructor
@Document(collection = "DevUser")
public class User {

    private String userId;
    private String nickName;

    @Builder
    public User(String userId, String nickName){
        this.userId = userId;
        this.nickName = nickName;
    }
}
