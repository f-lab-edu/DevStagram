package com.moondy.devstameetup.service;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.document.MeetUpCategory;
import com.moondy.devstameetup.domain.dto.AcceptMemberDto;
import com.moondy.devstameetup.domain.dto.JoinMeetUpDto;
import com.moondy.devstameetup.domain.dto.MeetUpSummaryDto;
import com.moondy.devstameetup.domain.dto.UpdateMeetUpDto;
import com.moondy.devstameetup.repository.MeetUpCategoryRepository;
import com.moondy.devstameetup.repository.MeetUpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    public MeetUp updateMeetUp(UpdateMeetUpDto dto, String userId) {
        isExistCategory(dto.getCategory());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(dto.getId()));
        query.addCriteria(Criteria.where("leaderId").is(userId));

        Update update = new Update();
        update.set("category", dto.getCategory());
        update.set("title", dto.getTitle());
        update.set("contents", dto.getContents());
        update.set("maxPeople", dto.getMaxPeople());
        update.set("isOpenYn", dto.getIsOpenYn());
        update.set("isRecruiting", dto.getIsRecruiting());
        return updateMember(query, update);
    }

    public Boolean deleteMeetUp(String id, String userId) {
        if (!meetUpRepository.findById(id).get().getLeaderId().equals(userId)) throw new CustomException(CommonCode.NO_PERMISSION);
        meetUpRepository.deleteById(id);
        return meetUpRepository.findById(id).isEmpty();
    }

    public MeetUp joinMeetUp(String userId, JoinMeetUpDto dto) {
        MeetUp target = getOneMeetUp(dto.getMeetUpId());
        Query query = new Query();
        Update update = new Update();
        //이미 참여중인지 확인, 이미 보류(밋업 대기)중인지 확인
        List<String> memberList = target.getMemberId();
        List<String> pendingList = target.getPendingId();
        if (memberList.contains(userId) || target.getLeaderId().equals(userId)) throw new CustomException(CommonCode.ALREADY_JOINED);
        if (pendingList.contains(userId)) throw new CustomException(CommonCode.ALREADY_PENDING);
        //추가
        if (target.getIsOpenYn()) {
            memberList.add(userId);
            query.addCriteria(Criteria.where("id").is(dto.getMeetUpId()));
            update.set("memberId", memberList);
        } else {
            pendingList.add(userId);
            query.addCriteria(Criteria.where("id").is(dto.getMeetUpId()));
            update.set("pendingId", pendingList);
        }
        return updateMember(query, update);
    }

    public MeetUp acceptMember(String userId, AcceptMemberDto dto) {
        MeetUp target = getOneMeetUp(dto.getMeetUpId());
        //리더인지 확인
        if (!target.getLeaderId().equals(userId)) throw new CustomException(CommonCode.NO_PERMISSION);
        // 대기중 리스트에 있는지 확인
        if (!target.getPendingId().contains(dto.getMemberId())) throw new CustomException(CommonCode.NOT_IN_PENDING_LIST);

        Query query = new Query();
        Update update = new Update();
        List<String> memberList = target.getMemberId();
        List<String> pendingList = target.getPendingId();

        query.addCriteria(Criteria.where("id").is(dto.getMeetUpId()));
        memberList.add(dto.getMemberId());
        pendingList.remove(dto.getMemberId());
        update.set("memberId", memberList);
        update.set("pendingId", pendingList);
        return updateMember(query, update);
    }

    public MeetUp removeMember(String userId, AcceptMemberDto dto) {
        MeetUp target = getOneMeetUp(dto.getMeetUpId());
        //리더인지 확인
        if (!target.getLeaderId().equals(userId)) throw new CustomException(CommonCode.NO_PERMISSION);

        Query query = new Query();
        Update update = new Update();
        List<String> memberList = target.getMemberId();
        List<String> pendingList = target.getPendingId();

        query.addCriteria(Criteria.where("id").is(dto.getMeetUpId()));

        memberList.removeIf(s -> s.equals(dto.getMemberId()));
        pendingList.removeIf(s -> s.equals(dto.getMemberId()));

        update.set("memberId", memberList);
        update.set("pendingId", pendingList);
        return updateMember(query, update);
    }

    private MeetUp updateMember(Query query, Update update) {
        // 수정 후 결과를 리턴해주도록 옵션 설정
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
        MeetUp meetUp = mongoTemplate.findAndModify(query, update, findAndModifyOptions, MeetUp.class);
        if (meetUp == null) throw new CustomException(CommonCode.UPDATE_FAILED);
        return meetUp;
    }
}
