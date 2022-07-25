package com.moondy.devstameetup.domain.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "MeetUpCategory")
@Getter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetUpCategory {
    @Id
    @Field("_id")
    private String code;

    @Field("display_name")
    private String displayName;
}
