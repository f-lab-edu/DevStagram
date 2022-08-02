package com.moondysmell.devstaposts.service;


import com.mongodb.client.result.UpdateResult;
import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.repository.PostsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
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
               // .updateDt(LocalDateTime.now())
                .build();

        //return mongoTemplate.insert(posts);
        return postsRepository.save(posts);
    }

    //타임라인 - 팔로워 조회
//    public List<Posts> viewAll(List<String> ids){
//        return postsRepository.findByIdInOrderByCreatedAtDesc(ids);
//    }

    //타임라인 - 전체조회(최신순)
    public List<Posts> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDt"));
        Query query = new Query()
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
                .limit(pageable.getPageSize());


        return mongoTemplate.find(query, Posts.class);
        //return postsRepository.findAll();
    }

    //내가 작성한 피트들만 조회
    //Pagenation적용
    public List<Posts> findAllById(String userId, int page, int size) {
        //Query query = new Query(Criteria.where("userId").is(userId)).with(Sort.by(Sort.Direction.DESC, "createDt"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDt"));
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize());

        return mongoTemplate.find(query, Posts.class);
    }

    //피드 하나만 조회
    public Posts getOneFeed(Long id) {
        return postsRepository.findById(id).orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
    }


    //게시글 update
    public Posts update(Long postId, String userId, PostsSaveRequestDto newPosts) {
        Optional<Posts> postById = postsRepository.findById(postId); //.orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
        //Posts posts = Optional.of(postsRepository.findById(postId).get()).orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
        if (postById.isEmpty()) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);

        Posts posts = postById.get();
        if (!posts.getUserId().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER);

        Query query = new Query(Criteria.where("id").is(postId));
        Update update = new Update();
        update.set("contents", newPosts.getContents());
        update.set("pictureUrl", newPosts.getPictureUrl());
        update.set("updateDt", LocalDateTime.now());

       FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true); //결과값 보기 옵션
        posts = mongoTemplate.findAndModify(query, update, options, Posts.class);
        if (posts == null) throw new CustomException(CommonCode.POST_UPDATE_FAIL);

        return mongoTemplate.findAndModify(query, update, options, Posts.class);

    }


    //피드 삭제
    public void delete(Long postId, String userId) {
        Optional<Posts> postById = postsRepository.findById(postId); //.orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
       // Optional<Posts> temp = postsRepository.findById(postId);
        //Posts posts = Optional.of(postsRepository.findById(postId).get()).orElseThrow(() -> new CustomException(CommonCode.NOT_FOUNT_CONTENTS));
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

        Posts posts = mongoTemplate.findAndModify(query, update, options, Posts.class);
        if (posts == null) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);

        return posts.getHeartsCount().size(); //좋아요 갯수 반환
    }
}
