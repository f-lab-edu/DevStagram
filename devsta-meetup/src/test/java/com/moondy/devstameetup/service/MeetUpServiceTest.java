package com.moondy.devstameetup.service;

import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.repository.MeetUpCategoryRepository;
import com.moondy.devstameetup.repository.MeetUpRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MeetUpServiceTest {


    @Test
    void getRecentMeetUpSummary() {
        MeetUpCategoryRepository meetUpCategoryRepository = Mockito.mock(MeetUpCategoryRepository.class);
        MeetUpRepository meetUpRepository = Mockito.mock(MeetUpRepository.class);
        MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);
        MeetUpService meetUpService = new MeetUpService(meetUpCategoryRepository, meetUpRepository, mongoTemplate);
        ObjectId tempId = new ObjectId();
        MeetUp newMeetUp = new MeetUp(tempId, "STUDY", "스터디 구합니다", "서울 스더티 구합니다", 5, null, null, true, true, null, LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(meetUpRepository.save(Mockito.any(MeetUp.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(meetUpRepository.findById(tempId.toString()))
                .thenReturn(Optional.of(newMeetUp));

//		List<MeetUp> meetUpList = meetUpService.getRecentMeetUpSummary(0,2);


        MeetUp savedMeetup = meetUpService.saveMeetUp(newMeetUp);
        System.out.println(savedMeetup);
        MeetUp target = meetUpService.getOneMeetUp(tempId.toString());

        assertEquals("STUDY", target.getCategory());

    }
}