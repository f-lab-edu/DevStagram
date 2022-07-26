package com.moondy.devstameetup.domain.dto;

import com.moondy.devstameetup.domain.document.MeetUp;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetUpDto {
    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    private String contents;

    @Max(value = 10, message = "멤버수는 최대 10명까지 가능합니다.")
    @Min(value = 0, message = "멤버수는 음수가 될 수 없습니다.")
    private Integer maxPeople;

    private Boolean isOpenYn = true;

    public MeetUp toDao(String userId) {
        return MeetUp.builder()
                .category(this.getCategory())
                .title(this.getTitle())
                .contents(this.getContents())
                .maxPeople(this.getMaxPeople())
                .memberId(List.of())
                .pendingId(List.of())
                .isOpenYn(this.getIsOpenYn())
                .isRecruiting(true)
                .leaderId(userId)
                .build();
    }
}
