package com.moondy.devstameetup.repository;


import com.moondy.devstameetup.domain.document.MeetUpCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetUpCategoryRepository extends MongoRepository<MeetUpCategory, String> {
}
