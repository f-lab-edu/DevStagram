package com.moondysmell.devstaposts.domain.document;

import com.moondysmell.devstaposts.domain.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "Comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comments  {

    @Transient
    public static final  String SEQUENCE_NAME = "comments_sequence";

    @Id
    @Field("_id")
    private Long id;

    @Field("post_id")
    @CreatedBy
    private Long postId;

    @Field("comment_user")
    private String commentUser;

    private String contents;

    @Field("create_dt")
    @CreatedDate
    private LocalDateTime createDt;


    @Field("update_dt")
    @LastModifiedDate
    private LocalDateTime updateDt;

//    public CommentDto toDto(){
//        return CommentDto.builder()
//                .postId(this.id.toString())
//                .contents(this.contents).build();
//    }
}
