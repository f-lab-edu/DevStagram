package com.moondysmell.devstausers.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
public class UserInitSaveDto {
    private String name;
    private String pictureUrl;
    private Date createdDt;
    private String email;
}
