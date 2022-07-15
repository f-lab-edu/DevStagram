package com.moondysmell.devstaposts.service;


import com.moondysmell.devstaposts.domain.Sequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
//새로운 Posts가 추가 될때 마다 자동으로 id값이 올라가도록 Listener클래스 작성
    private final MongoOperations mongoOperations;

    public long generateSequence(String seqName){
        Sequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true), Sequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
