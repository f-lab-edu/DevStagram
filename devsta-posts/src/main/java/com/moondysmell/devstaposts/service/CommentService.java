package com.moondysmell.devstaposts.service;

import com.mongodb.client.result.UpdateResult;
import com.moondysmell.devstaposts.domain.document.Comments;
import com.moondysmell.devstaposts.domain.document.Posts;
import com.moondysmell.devstaposts.domain.dto.CommentDto;
import com.moondysmell.devstaposts.exception.CommonCode;
import com.moondysmell.devstaposts.exception.CustomException;
import com.moondysmell.devstaposts.repository.CommentRepository;
import com.moondysmell.devstaposts.repository.PostsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final MongoTemplate mongoTemplate;
    final static private String COLLECTION_NAME="Comments";

    //댓글 작성
    public Comments createComment(CommentDto commentDto, String userId){

        Comments comments = Comments.builder()
                .postId(commentDto.getPostId())
                .commentUser(userId)
                .contents(commentDto.getContents())
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())

                .build();

        return mongoTemplate.insert(comments,COLLECTION_NAME);
    }

    //댓글모아보기
    public List<Comments> getComments(Long postId){
        Query query = new Query(Criteria.where("postId").is(postId)).with(Sort.by(Sort.Direction.DESC,"createDt"));
        List<Comments> comments = mongoTemplate.find(query, Comments.class);

        return comments;
    }

    public Comments updateComments(Long commentId, String userId, CommentDto commentDto){
        Comments comments = mongoTemplate.findById(commentId, Comments.class);


        if (comments == null) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS); //해당 댓글이 없을때 예외처리
        else if(comments.getContents().equals(commentDto.getContents())) throw new CustomException(CommonCode.COMMENT_NO_CHANGE); //변경사항이 없을때 예외처리
        else if (!comments.getCommentUser().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER); //작성한자와 수정하려는자가 동일하지 않을떄 예외처리
        else {
            Query query = new Query(Criteria.where("id").is(commentId)
                    .andOperator(Criteria.where("commentUser").is(userId)));
            Update update = new Update();
            update.set("contents", commentDto.getContents());
            update.set("updateDt", LocalDateTime.now());

            //UpdateResult updateResult
            comments = mongoTemplate.findAndModify(
                    query,
                    update,
                    Comments.class);

            //return comments;
        }
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(commentId)), Comments.class);
    }

    public boolean deleteComment(Long commentId, String userId){
        Comments comments = mongoTemplate.findById(commentId, Comments.class);
        if (comments == null) throw new CustomException(CommonCode.NOT_FOUNT_CONTENTS);
        else if (!comments.getCommentUser().equals(userId)) throw new CustomException(CommonCode.NOT_MATCH_WRITER); //작성한자와 수정하려는자가 동일하지 않을떄 예외처리
        else {
            //유저가 작성자 또는
            //(admin일때 삭제할수 있도록) - 추후개발
            postsRepository.deleteById(String.valueOf(commentId));
            return true;
        }

    }
}
