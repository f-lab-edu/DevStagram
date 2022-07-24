package com.moondy.devstameetup.config;

import com.moondy.devstameetup.controller.MeetUpController;
import com.moondy.devstameetup.domain.dto.MeetUpSummaryDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MeetUpSummaryAssembler implements RepresentationModelAssembler<MeetUpSummaryDto, EntityModel<MeetUpSummaryDto>> {

    @Override
    public EntityModel<MeetUpSummaryDto> toModel(MeetUpSummaryDto entity) {
        return EntityModel.of(entity, linkTo(methodOn(MeetUpController.class).getOneMeetUp(entity.getId())).withSelfRel());
    }

    public CollectionModel<EntityModel<MeetUpSummaryDto>> toCollection(List<MeetUpSummaryDto> entityList, String category, int page, int size) {
        // 각 Employee 객체마다 엔티티 모델 생성
        List<EntityModel<MeetUpSummaryDto>> detail = entityList.stream().map(meetUp -> {
            return EntityModel.of(meetUp,
                    // 각 엔티티 모델마다 링크 추가.
                    linkTo(methodOn(MeetUpController.class).getOneMeetUp(meetUp.getId())).withSelfRel());
        }).collect(Collectors.toList());

        // 엔티티 모델들과 별개로 listAll 메서드 링크 추가.
        return CollectionModel.of(detail,
                linkTo(methodOn(MeetUpController.class).getMeetSummaries(category, page + 1, size)).withRel("next"));
    }

    //
//    @Override
//    public EntityModel<Meetup> toModel(MeetUpSummaryDto meetUp) {
//        // 각 Employee 객체마다 엔티티 모델 생성
//
//        return EntityModel.of(meetUp,
//                // 각 엔티티 모델마다 링크 추가.
//                WebMvcLinkBuilder.linkTo(methodOn(MeetUpController.class).getOneMeetUp(meetUp.getId())).withSelfRel());//
////
//        // 엔티티 모델들과 별개로 listAll 메서드 링크 추가.
//        return CollectionModel.of(detail,
//                WebMvcLinkBuilder.linkTo(methodOn(MeetUpController.class).getMeetSummaries(category, page + 1, size)).withRel("next"));
//
//        return EntityModel.of(meetUp,
//                linkTo(methodOn(MeetUpController.class).getMeetSummaries(category, page + 1, size +1).withSelfRel(),
//                linkTo(methodOn(MeetUpController.class).listAll()).withRel("detail"));
//    }


}

