package com.moondysmell.devstaposts.domain.document;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@Document(collection = "Posts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Posts{

    @Transient
    public static final  String SEQUENCE_NAME = "posts_sequence";

    @Id
    @Field("_id")
    private Long id;

    //DevUser의 Nickname을 가져오기위함
    @Field("user_id")
    @CreatedBy
    private String userId;

    @Field
    private String contents;

    @Field("hearts_count")
    private List<String> heartsCount;

    @Field("picture_url")
    private String pictureUrl;

    @Field("create_dt")
    @CreatedDate
    private LocalDateTime createDt;


    @Field("update_dt")
    @LastModifiedDate
    private LocalDateTime updateDt;


    public String getId() {
        return id + "";
    }
}
