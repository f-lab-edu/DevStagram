package com.moondysmell.devstausers.domain.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private ObjectId roomId;

    private String sender;

    private String receiver;

    private String message;

    private LocalDateTime createDt;
}
