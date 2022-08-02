package com.moondysmell.devstausers.domain.document;

import java.util.ArrayList;
import java.util.List;

import com.moondysmell.devstausers.domain.dto.ChatMessageDto;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.*;

@Document(collection = "ChatRoom")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChatRoom {
    
    @Id
    @Field("_id")
    private ObjectId roomId;

    private List<String> members;

    @Field("last_message")
    private ChatMessage lastMessage;

    
}
