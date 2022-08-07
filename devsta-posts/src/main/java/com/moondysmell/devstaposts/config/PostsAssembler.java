package com.moondysmell.devstaposts.config;

import com.moondysmell.devstaposts.controller.PostsController;
import com.moondysmell.devstaposts.domain.document.Posts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostsAssembler implements RepresentationModelAssembler<Posts, EntityModel<Posts>> {
    @Value("${url.gateway}")
    private String GATEWAY_URL = "";

    @Override
    public EntityModel<Posts> toModel(Posts entity) {
        return EntityModel.of(entity, Link.of(String.format("%s/getOneFeed?id=%s", GATEWAY_URL, entity.getId()), "detail"));
    }

    public CollectionModel<EntityModel<Posts>> toCollection(List<Posts> entityList, Link nextLink){
        List<EntityModel<Posts>> detail = entityList.stream().map(posts ->
        { return EntityModel.of(posts,
                Link.of(String.format("%s/getOneFeed?id=%s", GATEWAY_URL, posts.getId()), "detail"));
        }).collect(Collectors.toList());

        return CollectionModel.of(detail, nextLink);
    }

}
