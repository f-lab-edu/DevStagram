package com.moondysmell.devstausers.domain.document;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ChatMessage")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChatMessage implements Serializable {

    @Id
    @Field("_id")
    private ObjectId id;

    @Field("room_id")
    private ObjectId roomId;

    private String sender;

    private String receiver;

    private String message;

    //private int isReadCount;

    @Field("create_dt")
    private LocalDateTime createDt;

}
