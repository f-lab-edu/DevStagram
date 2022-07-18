package com.moondy.devstameetup.domain.dto;

import lombok.*;


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
}
