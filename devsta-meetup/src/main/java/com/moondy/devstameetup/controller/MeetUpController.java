package com.moondy.devstameetup.controller;

import com.moondy.devstameetup.common.CommonCode;
import com.moondy.devstameetup.common.CommonResponse;
import com.moondy.devstameetup.common.CustomException;
import com.moondy.devstameetup.domain.document.MeetUp;
import com.moondy.devstameetup.domain.dto.CreateMeetUpDto;
import com.moondy.devstameetup.domain.dto.MeetUpDto;
import com.moondy.devstameetup.domain.dto.MeetUpSummaryDto;
import com.moondy.devstameetup.service.MeetUpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/meetup")
public class MeetUpController {
    private final MeetUpService meetUpService;

    @PostMapping("/create")
    public CommonResponse createMeetup(@RequestHeader("userId") String userId, @RequestBody CreateMeetUpDto meetUpDto) throws CustomException{
        //category 있는지 확인
        meetUpService.isExistCategory(meetUpDto.getCategory());
        MeetUp newMeetUp = meetUpService.saveMeetUp(meetUpDto.toDao(userId));
        return new CommonResponse(CommonCode.SUCCESS, Map.of("result", newMeetUp.toDto()));
    }

    @GetMapping("/getMeetUps")
    public CommonResponse getMeetUps(@RequestParam int fromPage, @RequestParam int toPage) {
        List<MeetUp> meetUpList = meetUpService.getRecentMeetUp(fromPage, toPage);
        return new CommonResponse(CommonCode.SUCCESS, Map.of("result", meetUpList));
    }

    @GetMapping("/getMeetUpSummaries")
    public CommonResponse getMeetSummaries(@RequestParam int fromPage, @RequestParam int toPage) {
        List<MeetUpSummaryDto> meetUpList = meetUpService.getRecentMeetUpSummary(fromPage, toPage);
        return new CommonResponse(CommonCode.SUCCESS, Map.of("result", meetUpList));
    }
}
