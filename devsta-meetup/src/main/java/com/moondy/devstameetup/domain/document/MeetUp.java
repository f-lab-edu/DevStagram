package com.moondy.devstameetup.domain.document;

import com.moondy.devstameetup.domain.dto.CreateMeetUpDto;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "MeetUp")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetUp {

    @Id
    @Field("_id")
    private ObjectId id;

    @Field("category")
    private String category;

    @Field("title")
    private String title;

    @Field("contents")
    private String contents;

    @Field("max_people")
    private Integer maxPeople;

    @Field("member_id")
    private List<String> memberId;

    @Field("is_open_yn")
    private Boolean isOpenYn;

    @Field("is_recruiting")
    private Boolean isRecruiting;

    @Field("leader_id")
    private String leaderId;

    public static MeetUp of(String userId, CreateMeetUpDto dto) {
        MeetUp meetup = MeetUp.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .maxPeople(dto.getMaxPeople())
                .memberId(List.of())
                .isOpenYn(dto.getIsOpenYn())
                .isRecruiting(true)
                .leaderId(userId)
                .build();
        return meetup;
    }

}
