package com.moondy.devstameetup.domain.document;

import com.moondy.devstameetup.domain.dto.MeetUpDto;
import com.moondy.devstameetup.domain.dto.MeetUpSummaryDto;
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

    @Field("pending_id")
    private List<String> pendingId;

    @Field("is_open_yn")
    private Boolean isOpenYn;

    @Field("is_recruiting")
    private Boolean isRecruiting;

    @Field("leader_id")
    private String leaderId;

    public MeetUpDto toDto() {
        return MeetUpDto.builder()
                .id(this.id.toString())
                .category(this.category)
                .title(this.title)
                .contents(this.contents)
                .maxPeople(this.maxPeople)
                .memberId(this.memberId)
                .pendingId(this.pendingId)
                .isOpenYn(this.isOpenYn)
                .isRecruiting(this.isRecruiting)
                .leaderId(this.leaderId)
                .build();
    }

    public MeetUpSummaryDto toSummaryDto() {
        return MeetUpSummaryDto.builder()
                .id(this.id.toString())
                .category(this.category)
                .title(this.title)
                .contents(this.contents.length() > 50 ? this.contents.substring(0,50) : this.contents )
                .maxPeople(this.maxPeople)
                .memberCount(this.memberId.size())
                .isOpenYn(this.isOpenYn)
                .isRecruiting(this.isRecruiting)
                .leaderId(this.leaderId)
                .build();
    }


}
