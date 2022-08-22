package com.moondysmell.devstaposts;

import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.repository.PostsRepository;
import com.moondysmell.devstaposts.service.PostsService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

//@ExtendWith({MockitoExtension.class, SpringExtension.class}) //가짜 객체 명시
@ExtendWith({MockitoExtension.class}) //가짜 객체 명시
public class PostServiceUnitTest {

    @InjectMocks
    private PostsService postsService;

    @Mock
    private PostsRepository postsRepository;

    @Test
    @DisplayName("삭제하려는 유저와 글을 작성한 유저가 다를경우 진행하지않는다.")
    public void deleteTest01(){


        //given
        String userId = "wrongUser";
        Posts posts = Posts.builder()
                .id(45L)
                .contents("happy")
                .userId("hi")
                .build();

        when(postsRepository.findById(any())).thenReturn(Optional.of(posts));

        Throwable throwable = assertThrows(CustomException.class, ()->{
            postsService.delete(any(), userId);
        });

       // assertEquals(CommonCode.NOT_MATCH_WRITER, throwable.getMessage());
        Assertions.assertEquals(CommonCode.NOT_MATCH_WRITER.getMessage(), throwable.getMessage());
        verify(postsRepository, never()).deleteById(any());
    }


    @Test
    @DisplayName("삭제하려는 유저와 글을 작성한 유저가 동일할 경우 삭제")
    public void deleteTest02(){

        Long postId = 45L;
        String userId = "writer";
        Posts posts = Posts.builder()
                .id(postId)
                .contents("happy")
                .userId("writer")
                .build();


        given(postsRepository.findById(postId)).willReturn(Optional.of(posts));
        doNothing().when(postsRepository).deleteById(any());


        postsService.delete(postId,userId);
        verify(postsRepository, times(1)).deleteById(any());
    }
}
