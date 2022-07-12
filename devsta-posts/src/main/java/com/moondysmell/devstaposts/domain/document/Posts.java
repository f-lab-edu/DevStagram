package com.moondysmell.devstaposts.domain.document;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Document(collection = "Posts")
@Data
@NoArgsConstructor
public class Posts  {

    @Transient
    public static final  String SEQUENCE_NAME = "posts_sequence";

    @Id
    @Field("_id")
    private Long id;

    //DevUser의 Nickname을 가져오기위함
    @Field("user_id")
    @CreatedBy
    private User user;

    @Setter
    private String contents;

    @Field("hearts_count")
    private String heartsCount;

    @Field("picture_url")
    private String pictureUrl;

    @Field("crate_dt")
    @CreatedDate
    private LocalDateTime createDt;

    @Field("update_dt")
    @LastModifiedDate
    private LocalDateTime updateDt;

    @Builder
    public Posts(User user, String contents){
        this.user = user;
        this.contents = contents;
    }

    public Posts update(Posts newPosts){
        this.contents = newPosts.contents;
        return this;
    }


}
