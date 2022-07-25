package com.moondy.devstameetup.repository;


import com.moondy.devstameetup.domain.document.MeetUp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeetUpRepository extends MongoRepository<MeetUp, String> {
    List<MeetUp> findAllByOrderByIdDesc();
}
