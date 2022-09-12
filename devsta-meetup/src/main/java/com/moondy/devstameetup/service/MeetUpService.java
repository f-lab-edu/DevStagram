package com.moondy.devstameetup.service;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.document.MeetUpCategory;
import com.moondy.devstameetup.domain.dto.*;
import com.moondy.devstameetup.repository.MeetUpCategoryRepository;
import com.moondy.devstameetup.repository.MeetUpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<MeetUp> getRecentMeetUpSummaryByCategory(int page, int size, String category) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
        Query query = new Query()
                .addCriteria(Criteria.where("category").is(category))
                .with(Sort.by(Sort.Direction.DESC, "id"))
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
                .limit(pageable.getPageSize());

        return mongoTemplate.find(query, MeetUp.class);
        //dto로 변환해서 리턴하고 싶을 때
//        return meetUpList.stream().map(it -> it.toSummaryDto()).collect(Collectors.toList());
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

   public Page<MeetUp> getRecentMeetUp(int page, int size) {
       Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
       Query query = new Query()
               .with(Sort.by(Sort.Direction.DESC, "id"))
               .with(pageable)
               .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
               .limit(pageable.getPageSize());


       List<MeetUp> meetUpList = mongoTemplate.find(query, MeetUp.class);
       return PageableExecutionUtils.getPage(
               meetUpList,
               pageable,
               () -> mongoTemplate.count(query.skip(-1).limit(-1),MeetUpSummaryDto.class)
               // query.skip(-1).limit(-1)의 이유는 현재 쿼리가 페이징 하려고 하는 offset 까지만 보기에 이를 맨 처음부터 끝까지로 set 해줘 정확한 도큐먼트 개수를 구한다.
       );

       //dto로 리턴하고 싶을 때
//       return meetUpList.stream().map(it -> it.toDto()).collect(Collectors.toList());
   }

    public List<MeetUp> getRecentMeetUpSummary(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
        Query query = new Query()
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
                .limit(pageable.getPageSize());

        return mongoTemplate.find(query, MeetUp.class);
       //summaryDto로 변환해서 리턴하고 싶을 때
//        return meetUpList.stream().map(it -> it.toSummaryDto()).collect(Collectors.toList());
    }


    public List<MeetUp> getRecentMyMeetUpSummary(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
        Query query = new Query()
                .addCriteria(Criteria.where("leaderId").is(userId))
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
                .limit(pageable.getPageSize());

        return mongoTemplate.find(query, MeetUp.class);

        //page로 리턴하고 싶을 때는 이렇게 사용
//        return PageableExecutionUtils.getPage(
//                meetUpList.stream().map(it -> it.toSummaryDto()).collect(Collectors.toList()),
//                pageable,
//                () -> mongoTemplate.count(query.skip(-1).limit(-1),MeetUpSummaryDto.class)
//                // query.skip(-1).limit(-1)의 이유는 현재 쿼리가 페이징 하려고 하는 offset 까지만 보기에 이를 맨 처음부터 끝까지로 set 해줘 정확한 도큐먼트 개수를 구한다.
//        );

    }

    public List<MeetUp> getJoinedMeetUpSummary(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));

        Criteria criteria = new Criteria();
        // memberId 또는 pendingId에 user가 있으면
        criteria.orOperator(Criteria.where("memberId").all(userId), Criteria.where("pendingId").all(userId));

        Query query = new Query()
                .addCriteria(criteria)
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset
                .limit(pageable.getPageSize());

        return mongoTemplate.find(query, MeetUp.class);

//        return meetUpList.stream().map(it -> it.toSummaryDto()).collect(Collectors.toList());
    }

    public List<MeetUpCategory> getCategory() {
        return meetUpCategoryRepository.findAll();
    }

    public MeetUp getOneMeetUp(String meetUpId) {
        Optional<MeetUp> meetup = meetUpRepository.findById(meetUpId);
        return meetup.orElseThrow(() -> new CustomException(CommonCode.MEETUP_NOT_EXIST));
    }

    public MeetUp updateMeetUp(UpdateMeetUpDto dto, String userId) {
        Query query = new Query();
        Update update = new Update();
        isExistCategory(dto.getCategory());

        MeetUp target = getOneMeetUp(dto.getId());
        // 리더가 아닌 경우 권한없음
        if (!target.getLeaderId().equals(userId)) throw new CustomException(CommonCode.NO_PERMISSION);
        // private -> Meetup으로 바뀌는 경우 pendingId에 있는 아이디를 memberId로 모두 이전
        if (target.getIsOpenYn() == false && dto.getIsOpenYn() == true) {
            List<String> memberList = target.getMemberId();
            List<String> pendingList = target.getPendingId();
            memberList.addAll(pendingList);
            pendingList.clear();
            update.set("memberId", memberList);
            update.set("pendingId", pendingList);
        }

        query.addCriteria(Criteria.where("id").is(dto.getId()));
        query.addCriteria(Criteria.where("leaderId").is(userId));

        update.set("category", dto.getCategory());
        update.set("title", dto.getTitle());
        update.set("contents", dto.getContents());
        update.set("maxPeople", dto.getMaxPeople());
        update.set("isOpenYn", dto.getIsOpenYn());
        update.set("isRecruiting", dto.getIsRecruiting());
        update.set("updatedDt", LocalDateTime.now());
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
            update.set("updatedDt", LocalDateTime.now());
        } else {
            pendingList.add(userId);
            query.addCriteria(Criteria.where("id").is(dto.getMeetUpId()));
            update.set("pendingId", pendingList);
            update.set("updatedDt", LocalDateTime.now());
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
        update.set("updatedDt", LocalDateTime.now());
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
        update.set("updatedDt", LocalDateTime.now());
        return updateMember(query, update);
    }

    public MeetUp removeSelf(String userId, JoinMeetUpDto dto) {
        MeetUp target = getOneMeetUp(dto.getMeetUpId());
        // 참여중이거나 대기중인 멤버인지 확인
        List<String> memberList = target.getMemberId();
        List<String> pendingList = target.getPendingId();
        if (!memberList.contains(userId) && !pendingList.contains(userId)) throw new CustomException(CommonCode.NOT_IN_PENDING_LIST);

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("id").is(dto.getMeetUpId()));

        memberList.removeIf(s -> s.equals(userId));
        pendingList.removeIf(s -> s.equals(userId));

        update.set("memberId", memberList);
        update.set("pendingId", pendingList);
        update.set("updatedDt", LocalDateTime.now());
        return updateMember(query, update);
    }

    private MeetUp updateMember(Query query, Update update) {
        // 수정 후 결과를 리턴해주도록 옵션 설정
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
        MeetUp meetUp = mongoTemplate.findAndModify(query, update, findAndModifyOptions, MeetUp.class);
        if (meetUp == null) throw new CustomException(CommonCode.UPDATE_FAILED);
        return meetUp;
    }

    public String getMeetUpStatus(String userId, String meetUpId) {
        MeetUp target = getOneMeetUp(meetUpId);
        if (target.getLeaderId().equals(userId)){
            return OWNED;
        } else if (target.getMemberId().contains(userId)) {
            return JOINED;
        } else if (target.getPendingId().contains(userId)) {
            return PENDING;
        }
        return UNRELATED;
    }

    //오타 줄이기 위해. 만약 코드가 아닌 display name 으로 리턴해야하면 value를 수정
    private static final String OWNED = "OWNED";
    private static final String JOINED = "JOINED";
    private static final String PENDING = "PENDING";
    private static final String UNRELATED = "UNRELATED";
}
