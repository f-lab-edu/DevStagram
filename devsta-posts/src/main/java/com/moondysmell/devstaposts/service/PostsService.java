package com.moondysmell.devstaposts.service;


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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final MongoTemplate mongoTemplate;
    final static private String COLLECTION_NAME="Posts";


//    @Autowired
//    private MongoTemplate mongoTemplate;
//    private MongoOperations mongoOperations;

//    public PostsService(PostsRepository postsRepository) {
//        this.postsRepository = postsRepository;
//    }

    //게시글 저장
    public Posts save(PostsSaveRequestDto postsSaveRequestDto){
        String contents = postsSaveRequestDto.getContents();
        if(contents.isEmpty()) throw new CustomException(CommonCode.CONTENT_IS_MANDATORY);
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
//    public List<Posts> findAllById(String userId){
//        Query query = new Query(Criteria.where("userId").is(userId));
//        List<Posts> posts = mongoTemplate.find(query,Posts.class);
//        return posts;
//    }





}
