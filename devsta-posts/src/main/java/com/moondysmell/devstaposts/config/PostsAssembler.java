package com.moondysmell.devstaposts.config;

import com.moondysmell.devstaposts.controller.PostsController;
import com.moondysmell.devstaposts.domain.document.Posts;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PostsAssembler implements RepresentationModelAssembler<Posts, EntityModel<Posts>> {


    @Override
    public EntityModel<Posts> toModel(Posts entity) {
        return EntityModel.of(entity, linkTo(methodOn(PostsController.class).getOneFeed(entity.getId())).withSelfRel());
    }

    public CollectionModel<EntityModel<Posts>> toCollection(List<Posts> entityList, Link nextLink){
        List<EntityModel<Posts>> detail = entityList.stream().map(posts ->
        { return EntityModel.of(posts,
                linkTo(methodOn(PostsController.class).getOneFeed(posts.getId())).withSelfRel());
        }).collect(Collectors.toList());

        return CollectionModel.of(detail, nextLink);
    }

}
