package com.moondy.devstameetup.domain.document;

import com.moondy.devstameetup.domain.dto.CreateMeetUpDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "MeetUp")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetUp implements Persistable<Long> {
    @Transient
    public static final  String SEQUENCE_NAME = "meetup_sequence";

    @Id
    @Field("_id")
    private Long seq;

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

    @Field("how_join")
    private String howJoin;

    @Field("status")
    private String status;

    @Field("leader_id")
    private String leaderId;

    @Override
    public Long getId() {
        return this.seq;
    }

    @Override
    public boolean isNew() {
        return false;
    }

//    public MeetUp of(String userId, CreateMeetUpDto dto) {
//        MeetUp meetup = MeetUp.builder()
//                .
//    }
}
