package com.moondy.devstameetup.service;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.dto.CreateMeetUpDto;
import com.moondy.devstameetup.domain.dto.MeetUpDto;
import com.moondy.devstameetup.domain.dto.MeetUpSummaryDto;
import com.moondy.devstameetup.repository.MeetUpCategoryRepository;
import com.moondy.devstameetup.repository.MeetUpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MeetUpService {
    private final MeetUpCategoryRepository meetUpCategoryRepository;
    private final MeetUpRepository meetUpRepository;
    private final MongoTemplate mongoTemplate;

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

   public MeetUp saveMeetUp(MeetUp meetUp) {
       return meetUpRepository.save(meetUp);
   }

   public List<MeetUp> getRecentMeetUp(int fromPage, int toPage) {
       return mongoTemplate.find(new Query().with(Sort.by(Sort.Direction.DESC, "id")).with(PageRequest.of(fromPage, toPage)), MeetUp.class);
       //dto로 리턴하고 싶을 때
//       return meetUpList.stream().map(it -> it.toDto()).collect(Collectors.toList());
   }

    public List<MeetUpSummaryDto> getRecentMeetUpSummary(int fromPage, int toPage) {
        List<MeetUp> meetUpList = mongoTemplate.find(new Query().with(Sort.by(Sort.Direction.DESC, "id")).with(PageRequest.of(fromPage, toPage)), MeetUp.class);
       return meetUpList.stream().map(it -> it.toSummaryDto()).collect(Collectors.toList());
    }



}
