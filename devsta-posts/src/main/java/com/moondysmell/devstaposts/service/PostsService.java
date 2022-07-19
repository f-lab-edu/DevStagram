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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final MongoTemplate mongoTemplate;
    final static private String COLLECTION_NAME = "Posts";


    //게시글 저장
    public Posts savePost(PostsSaveRequestDto postsSaveRequestDto, String userId) {
        List<String> heartsCount = new LinkedList<>();

        Posts posts = Posts.builder()
                .userId(userId)
                .heartsCount(heartsCount)
                .contents(postsSaveRequestDto.getContents())
                .pictureUrl(postsSaveRequestDto.getPictureUrl())
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();

        //return mongoTemplate.insert(posts);
        return postsRepository.save(posts);
    }

    //타임라인 - 팔로워 조회
//    public List<Posts> viewAll(List<String> ids){
//        return postsRepository.findByIdInOrderByCreatedAtDesc(ids);
//    }

    //타임라인 - 전체조회(최신순)
    public List<Posts> viewAll() {
        return postsRepository.findAll(Sort.by(Sort.Direction.DESC, "createDt"));
    }

    //내가 작성한 피트들만 조회
    public List<Posts> findAllById(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId)).with(Sort.by(Sort.Direction.DESC, "createDt"));
        List<Posts> posts = mongoTemplate.find(query, Posts.class);
        return posts;
    }

    //게시글 update
    public Posts update(String postId, String userId, PostsSaveRequestDto newPosts) {
        Optional<Posts> postById = postsRepository.findById(postId); //.orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
        if (postById.isEmpty()) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);

        Posts posts = postById.get();
        if (!posts.getUserId().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER);

        Query query = new Query(Criteria.where("id").is(postId).andOperator(Criteria.where("userId").is(userId)));
        Update update = new Update();
        update.set("contents", newPosts.getContents());
        update.set("pictureUrl", newPosts.getPictureUrl());
        update.set("updateDt", LocalDateTime.now());

        posts = mongoTemplate.findAndModify(query, update, Posts.class);

        //if (updateResult.getModifiedCount() == 0) throw new CustomException(CommonCode.POST_UPDATE_FAIL);
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(postId)), Posts.class);
    }


    //피드 삭제
    public void delete(String postId, String userId) {
        Optional<Posts> postById = postsRepository.findById(postId); //.orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
        if (postById.isEmpty()) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);

        Posts posts = postById.get();
        //유저가 작성자 또는
        // (admin일때 삭제 하도록) - 추후개발
        if (!posts.getUserId().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER);

        postsRepository.deleteById(postId);
    }

    public boolean isLikeUser(Long postId, String userId) {
        Query query = new Query(Criteria.where("id").is(postId).andOperator(Criteria.where("heartsCount").is(userId)));
        List<Posts> likeUser = mongoTemplate.find(query, Posts.class);

        //whether user exist in hearCount Field of posts collection
        //true : like 안누른 유저
        //false : like 이미 누른 유저
        return (likeUser.isEmpty()) ? true : false;
    }

    public int like(Long postId, String userId) {

        boolean isLikeUser = isLikeUser(postId, userId);//

        Update update = null;
        if (!isLikeUser) { // 이미 눌렀으면 좋아요-
            update = new Update();
            update.pull("heartsCount", userId);
        } else { //처음이면 좋아요+
            update = new Update();
            update.addToSet("heartsCount", userId); //중복 제거
        }

        Query query = new Query(Criteria.where("id").is(postId));
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true); //결과값 보기 옵션

        Posts post = mongoTemplate.findAndModify(query, update, options, Posts.class);

        return post.getHeartsCount().size(); //좋아요 갯수 반환
    }
}
