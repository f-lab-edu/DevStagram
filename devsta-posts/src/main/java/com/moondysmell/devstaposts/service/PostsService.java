package com.moondysmell.devstaposts.service;


import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.document.User;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.ExceptionType;
import com.moondysmell.devstaposts.exception.PostsException;
import com.moondysmell.devstaposts.repository.PostsRepository;
import com.moondysmell.devstaposts.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private UserRepository userRepository;

//    @Autowired
//    private MongoTemplate mongoTemplate;
//    private MongoOperations mongoOperations;

    public PostsService(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    public Posts post(PostsSaveRequestDto postsSaveRequestDto, User user){
        user = Optional.of(userRepository.findByUserId(user.getUserId())).orElseThrow(() -> new PostsException(ExceptionType.NOT_FOUND_USER));
        return postsRepository.save(postsSaveRequestDto.convertPosts(user));
    }


//    public Posts createPost(PostsSaveRequestDto postsSaveRequestDto){
//
//            Posts posts = new Posts(postsSaveRequestDto.getContents());
//            postsRepository.save(posts);
//
//            Query query = new Query(Criteria.where("user_id").is(posts.getDevUser()))
////            return posts;
//
//        }


}
