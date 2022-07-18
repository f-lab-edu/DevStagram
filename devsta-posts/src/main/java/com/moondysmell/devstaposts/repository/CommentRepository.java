package com.moondysmell.devstaposts.repository;


import com.moondysmell.devstaposts.domain.document.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;


public interface CommentRepository extends MongoRepository<Comments, String> {
     List<Comments> findAllByPostId(Long postId);
}
