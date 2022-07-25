package com.moondy.devstameetup.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMeetUpDto {
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private String id;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    private String contents;

    @Max(value = 10, message = "멤버수는 최대 10명까지 가능합니다.")
    @Min(value = 0, message = "멤버수는 음수가 될 수 없습니다.")
    private Integer maxPeople;

    @NotNull
    private Boolean isOpenYn;

    @NotNull
    private Boolean isRecruiting;
}
