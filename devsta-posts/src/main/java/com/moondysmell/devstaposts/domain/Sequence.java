package com.moondysmell.devstaposts.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "DBsequences")
public class Sequence {

    @Id
    private String id;
    private Long seq;

}
