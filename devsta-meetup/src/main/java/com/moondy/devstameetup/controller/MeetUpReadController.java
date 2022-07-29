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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Slf4j
@RequestMapping("/meetup/read")
public class MeetUpReadController {
    private final MeetUpService meetUpService;
    private static final String RESULT = "result";
    private static final String CATEGORY_ALL = "ALL";
    private final MeetUpSummaryAssembler meetUpSummaryAssembler;


    public MeetUpReadController(MeetUpService meetUpService, MeetUpSummaryAssembler meetUpSummaryAssembler) {
        this.meetUpService = meetUpService;
        this.meetUpSummaryAssembler = meetUpSummaryAssembler;
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
                linkTo(methodOn(MeetUpReadController.class).getMeetSummaries(category, page + 1, size)).withRel("next"));
    }

    @GetMapping("/getOneMeetUp")
    public CommonResponse getOneMeetUp(@RequestParam String meetUpId) {
        MeetUp meetUp = meetUpService.getOneMeetUp(meetUpId);
        return new CommonResponse(CommonCode.SUCCESS, Map.of(RESULT, meetUp.toDto()));
    }

}
