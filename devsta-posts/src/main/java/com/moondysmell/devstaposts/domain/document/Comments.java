package com.moondysmell.devstaposts.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comments implements Persistable<Long> {

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
    private Date createDt;


    @Field("update_dt")
    @LastModifiedDate
    private Date updateDt;

    @Override
    public boolean isNew()  {
        return createDt == null;
    }
}
