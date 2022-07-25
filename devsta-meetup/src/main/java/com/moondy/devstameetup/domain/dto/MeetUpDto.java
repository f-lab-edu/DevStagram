package com.moondy.devstameetup.domain.dto;

import com.moondy.devstameetup.domain.document.MeetUp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetUpDto {
    private String id;
    private String category;
    private String title;
    private String contents;
    private Integer maxPeople;
    private List<String> memberId;
    private List<String> pendingId;
    private Boolean isOpenYn;
    private Boolean isRecruiting;
    private String leaderId;


}
