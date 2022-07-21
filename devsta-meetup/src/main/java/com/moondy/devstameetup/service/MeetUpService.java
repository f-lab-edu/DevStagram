package com.moondy.devstameetup.service;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.document.MeetUpCategory;
import com.moondy.devstameetup.domain.dto.MeetUpSummaryDto;
import com.moondy.devstameetup.repository.MeetUpCategoryRepository;
import com.moondy.devstameetup.repository.MeetUpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MeetUpService {
    private final MeetUpCategoryRepository meetUpCategoryRepository;
    private final MeetUpRepository meetUpRepository;
    private final MongoTemplate mongoTemplate;

    public List<MeetUpSummaryDto> getRecentMeetUpSummaryByCategory(int fromPage, int toPage, String category) {
        Query query = new Query(Criteria.where("category").is(category));
        List<MeetUp> meetUpList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "id")).with(PageRequest.of(fromPage, toPage)), MeetUp.class);
        return meetUpList.stream().map(it -> it.toSummaryDto()).collect(Collectors.toList());
   }

   public Boolean isExistCategory(String categoryCode) {
        if (meetUpCategoryRepository.findById(categoryCode).isEmpty()) {
            throw new CustomException(CommonCode.CATEGORY_VALUE_NOT_EXIST);
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


    public List<MeetUpCategory> getCategory() {
        return meetUpCategoryRepository.findAll();
    }

    public MeetUp getOneMeetUp(String meetUpId) {
        Optional<MeetUp> meetup = meetUpRepository.findById(meetUpId);
        return meetup.orElseThrow(() -> new CustomException(CommonCode.MEETUP_NOT_EXIST));
    }
}
