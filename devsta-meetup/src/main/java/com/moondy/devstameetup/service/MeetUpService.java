package com.moondy.devstameetup.service;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.dto.CreateMeetUpDto;
import com.moondy.devstameetup.repository.MeetUpCategoryRepository;
import com.moondy.devstameetup.repository.MeetUpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MeetUpService {
    private final MeetUpCategoryRepository meetUpCategoryRepository;
    private final MeetUpRepository meetUpRepository;
    private final MongoTemplate mongoTemplate;
    final static private String CATEGORY_COLLECTION_NAME = "MeetUpCategory";
    final static private String MEETUP_COLLECTION_NAME = "MeetUp";

    //안쓰는 함수. 언제 쓸지 모르니 킵
//    public String getCategoryCode(String categoryDisplay) {
//        Query query = new Query(Criteria.where("displayName").is(categoryDisplay));
//        MeetUpCategory category = mongoTemplate.findOne(query, MeetUpCategory.class);
//        if (category == null) throw new CustomException(CommonCode.NOT_EXIST_CATEGORY_VALUE);
//        return category.getCode();
//   }

   public Boolean isExistCategory(String categoryCode) {
        if (meetUpCategoryRepository.findById(categoryCode).isEmpty()) {
            throw new CustomException(CommonCode.NOT_EXIST_CATEGORY_VALUE);
        }
        return true;
   }

   public MeetUp saveMeetUp(String userId, CreateMeetUpDto dto) {
       MeetUp newMeetUp = MeetUp.of(userId, dto);
       return meetUpRepository.save(newMeetUp);
   }

}
