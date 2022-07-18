package com.moondy.devstameetup.repository;


import com.moondy.devstameetup.domain.document.MeetUp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetUpRepository extends MongoRepository<MeetUp, String> {
}
