package com.moondy.devstameetup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetUpSummaryDto extends RepresentationModel<MeetUpSummaryDto> {
    private String id;
    private String category;
    private String title;
    private String contents;
    private Integer maxPeople;
    private Integer memberCount;
    private Boolean isOpenYn;
    private Boolean isRecruiting;
    private String leaderId;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;
}
