package com.moondysmell.devstaposts.repository;


import com.moondysmell.devstaposts.domain.document.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PostsRepository extends MongoRepository<Posts, String> {

}
