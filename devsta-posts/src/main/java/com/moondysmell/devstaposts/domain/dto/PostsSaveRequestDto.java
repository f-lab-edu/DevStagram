package com.moondysmell.devstaposts.domain.dto;

import com.moondysmell.devstaposts.domain.document.Posts;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
public class PostsSaveRequestDto {

    @NotBlank(message = "내용을 작성해주세요.")
    private String contents;
    private String pictureUrl;

}
