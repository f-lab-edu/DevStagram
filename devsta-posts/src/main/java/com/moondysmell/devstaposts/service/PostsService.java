package com.moondysmell.devstaposts.service;


import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.repository.PostsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
