package com.moondy.devstameetup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMeetUpDto {
    private String id;
    private String category;
    private String title;
    private String contents;
    private Integer maxPeople;
    private Boolean isOpenYn;
    private Boolean isRecruiting;
}
