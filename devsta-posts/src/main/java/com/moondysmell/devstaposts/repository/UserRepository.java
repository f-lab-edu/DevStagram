package com.moondysmell.devstaposts.repository;

import com.moondysmell.devstaposts.domain.document.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserId(String userId);

}
