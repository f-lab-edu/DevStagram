package com.moondy.devstameetup.controller;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CommonResponse;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.config.MeetUpSummaryAssembler;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.document.MeetUpCategory;
import com.moondy.devstameetup.domain.dto.*;
import com.moondy.devstameetup.service.MeetUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/meetup")
public class MeetUpController {
    private final MeetUpService meetUpService;
    private static final String RESULT = "result";
    private static final String CATEGORY_ALL = "ALL";
    private final MeetUpSummaryAssembler meetUpSummaryAssembler;
    @Value("${url.gateway}")
    private String GATEWAY_URL = "";

    public MeetUpController(MeetUpService meetUpService, MeetUpSummaryAssembler meetUpSummaryAssembler) {
        this.meetUpService = meetUpService;
        this.meetUpSummaryAssembler = meetUpSummaryAssembler;
    }

    @PostMapping("/create")
    public CommonResponse createMeetup(@RequestHeader("userId") String userId, @RequestBody @Valid CreateMeetUpDto meetUpDto) throws CustomException{
        //category 있는지 확인. 잘못된 카테고리로 create하면 추후 조회 안될 수 있으니 검사
        meetUpService.isExistCategory(meetUpDto.getCategory());
        MeetUp newMeetUp = meetUpService.saveMeetUp(meetUpDto.toDao(userId));
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, newMeetUp.toDto()));
    }

    @GetMapping("/getCategories")
    public CommonResponse getCategoryList() {
        List<MeetUpCategory> categoryList = meetUpService.getCategory();
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, categoryList));
    }

    @GetMapping("/getMeetUpSummaries/{category}")
    public CollectionModel<EntityModel<MeetUpSummaryDto>> getMeetSummaries(@PathVariable("category") String category, @RequestParam int page, @RequestParam int size) {
        // FE만 요청할 것이므로 카테고리가 정확하게 들어온다고 가정하고 코드를 작성. READ라 많이 호출될 것이기 때문에 DB 조회 최소화
        // 대문자로 변환
        String categoryUpper = category.toUpperCase();
        List<MeetUp> meetUpList;
        if (categoryUpper.equals(CATEGORY_ALL)) {
            meetUpList = meetUpService.getRecentMeetUpSummary(page, size);
        } else {
            meetUpList = meetUpService.getRecentMeetUpSummaryByCategory(page, size, categoryUpper);
        }
        return meetUpSummaryAssembler.toCollection(meetUpList,
                Link.of(String.format("%s/getMeetUpSummaries/%s?page=%s&size=%s", GATEWAY_URL, category, page + 1, size), "next"));
    }

    @GetMapping("/getMyMeetUp")
    public CollectionModel<EntityModel<MeetUpSummaryDto>> getMyMeetUp(@RequestHeader("userId") String userId, @RequestParam int page, @RequestParam int size) {
        //page는 0번부터 시작
        List<MeetUp> meetUpList = meetUpService.getRecentMyMeetUpSummary(userId, page, size);
        return meetUpSummaryAssembler.toCollection(meetUpList,
                Link.of(String.format("%s/getMyMeetUp?page=%s&size=%s", GATEWAY_URL, page + 1, size), "next"));
    }

    @GetMapping("/getJoinedMeetUp")
    public CollectionModel<EntityModel<MeetUpSummaryDto>> getJoinedMeetUp(@RequestHeader("userId") String userId,@RequestParam int page, @RequestParam int size) {
        List<MeetUp> meetUpList = meetUpService.getJoinedMeetUpSummary(userId, page, size);
        return meetUpSummaryAssembler.toCollection(meetUpList,
                Link.of(String.format("%s/getMyMeetUp?page=%s&size=%s", GATEWAY_URL, page + 1, size), "next"));
    }

    @GetMapping("/getOneMeetUp")
    public CommonResponse getOneMeetUp(@RequestParam String meetUpId) {
        MeetUp meetUp = meetUpService.getOneMeetUp(meetUpId);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, meetUp.toDto()));
    }

    @GetMapping("/getMeetUpStatus")
    public CommonResponse getMeetUpStatus(@RequestHeader("userId") String userId, @RequestParam String meetUpId) {
        String status = meetUpService.getMeetUpStatus(userId, meetUpId);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, status));
    }

    @PostMapping("/update")
    public CommonResponse updateMeetUp(@RequestHeader("userId") String userId, @RequestBody @Valid UpdateMeetUpDto dto) {
        MeetUp result = meetUpService.updateMeetUp(dto, userId);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, result.toDto()));
    }

    @DeleteMapping("/delete")
    public CommonResponse deleteMeetUp(@RequestHeader("userId") String userId, @RequestParam String id){
        Boolean result = meetUpService.deleteMeetUp(id, userId);
        if (result){
            return new CommonResponse(CommonCode.SUCCESS);
        }
        return new CommonResponse(CommonCode.DELETE_FAILED);
    }

    @PostMapping("/join")
    public CommonResponse joinMeetUp(@RequestHeader("userId") String userId, @RequestBody JoinMeetUpDto dto) {
        MeetUp meetUp = meetUpService.joinMeetUp(userId, dto);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, meetUp.toDto()));
    }

    @PostMapping("/accept")
    public CommonResponse acceptMember(@RequestHeader("userId") String userId, @RequestBody AcceptMemberDto dto) {
        MeetUp meetUp = meetUpService.acceptMember(userId, dto);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, meetUp.toDto()));
    }

    @PostMapping("/leave")
    public CommonResponse removeMember(@RequestHeader("userId") String userId, @RequestBody AcceptMemberDto dto) {
        MeetUp meetUp = meetUpService.removeMember(userId, dto);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, meetUp.toDto()));
    }



}
