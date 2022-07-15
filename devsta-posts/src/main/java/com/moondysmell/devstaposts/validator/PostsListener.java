package com.moondysmell.devstaposts.validator;


import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostsListener extends AbstractMongoEventListener<Posts> {
    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Posts> event){
        event.getSource().setId(generatorService.generateSequence(Posts.SEQUENCE_NAME));
    }
}
