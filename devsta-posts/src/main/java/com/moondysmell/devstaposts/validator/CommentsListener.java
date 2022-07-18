package com.moondysmell.devstaposts.validator;

import com.moondysmell.devstaposts.domain.document.Comments;
import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentsListener extends AbstractMongoEventListener<Comments> {

    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Comments> event){
        event.getSource().setId(generatorService.generateSequence(Comments.SEQUENCE_NAME));
    }
}
