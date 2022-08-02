package com.moondysmell.devstaposts.repository;


import com.moondysmell.devstaposts.domain.document.Comments;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;


public interface CommentRepository extends MongoRepository<Comments, Long> {

}
