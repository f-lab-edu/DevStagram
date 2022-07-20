package com.moondy.devstameetup.domain.dto;

import com.moondy.devstameetup.domain.document.MeetUp;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetUpDto {

    private String category;

    private String title;

    private String contents;

    private Integer maxPeople;

    private Boolean isOpenYn;

    public MeetUp toDao(String userId) {
        return MeetUp.builder()
                .category(this.getCategory())
                .title(this.getTitle())
                .contents(this.getContents())
                .maxPeople(this.getMaxPeople())
                .memberId(List.of())
                .isOpenYn(this.getIsOpenYn())
                .isRecruiting(true)
                .leaderId(userId)
                .build();
    }
}
