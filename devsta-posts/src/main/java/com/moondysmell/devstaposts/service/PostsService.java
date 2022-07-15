package com.moondysmell.devstaposts.service;


import com.mongodb.client.result.UpdateResult;
import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.repository.PostsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final MongoTemplate mongoTemplate;
    final static private String COLLECTION_NAME="Posts";


    //게시글 저장
    public Posts save(PostsSaveRequestDto postsSaveRequestDto){
        return mongoTemplate.insert(postsSaveRequestDto.toEntity(),COLLECTION_NAME);
    }

    //타임라인 - 팔로워 조회
//    public List<Posts> viewAll(List<String> ids){
//        return postsRepository.findByIdInOrderByCreatedAtDesc(ids);
//    }

    //타임라인 - 전체조회(최신순)
    public List<Posts> viewAll(){
        return postsRepository.findAll(Sort.by(Sort.Direction.DESC, "createDt"));
    }

    //내가 작성한 피트들만 조회
    public List<Posts> findAllById(String userId){
        Query query = new Query(Criteria.where("userId").is(userId)).with(Sort.by(Sort.Direction.DESC,"createDt"));
        List<Posts> posts = mongoTemplate.find(query,Posts.class);
        return posts;
    }

    //게시글 update
    public Posts update(String postId, String userId,  PostsSaveRequestDto newPosts) {
        Optional<Posts> postById = postsRepository.findById(postId); //.orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
        if (postById.isEmpty()) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);

        Posts posts = postById.get();
        if (!posts.getUserId().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER);

        Query query = new Query(Criteria.where("id").is(postId)
                .andOperator(Criteria.where("userId").is(userId)));
        Update update = new Update();
        update.set("contents", newPosts.getContents());
        update.set("pictureUrl", newPosts.getPictureUrl());

        UpdateResult updateResult = mongoTemplate.upsert(
                query,
                update,
                Posts.class);
        if (updateResult.getModifiedCount() == 0) throw new CustomException(CommonCode.POST_UPDATE_FAIL);
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(postId)), Posts.class);
    }


    //피드 삭제
    public void delete(String postId, String userId){
        Optional<Posts> postById = postsRepository.findById(postId); //.orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
        if (postById.isEmpty()) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);

        Posts posts = postById.get();
        //유저가 작성자 또는
        // (admin일때 삭제 하도록) - 추후개발
        if (!posts.getUserId().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER);



        postsRepository.deleteById(postId);
    }
}
