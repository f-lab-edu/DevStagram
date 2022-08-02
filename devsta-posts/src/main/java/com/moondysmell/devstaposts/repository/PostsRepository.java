package com.moondysmell.devstaposts.repository;


import com.moondysmell.devstaposts.domain.document.Posts;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostsRepository extends MongoRepository<Posts, Long> {

}
