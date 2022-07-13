package com.moondysmell.devstausers.service;

import com.mongodb.client.result.UpdateResult;
import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CustomException;
import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.ChangePwDto;
import com.moondysmell.devstausers.domain.dto.UserJoinDto;
import com.moondysmell.devstausers.repository.DevUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DevUserService {
    private final DevUserRepository devUserRepository;
    private final MongoTemplate mongoTemplate;

    final static private String COLLECTION_NAME="DevUser";

    public DevUser findUserByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        DevUser targetUser = mongoTemplate.findOne(query, DevUser.class);
        return targetUser;
    }

    public void checkExistUser(String email, String password) {
        DevUser user = findUserByEmail(email);
        if (user == null) throw new CustomException(CommonCode.NOT_EXIST_ID);
        // 나중에는 보안처리를 위해 pw는 hash로 저장할 예정. 그렇게 된다면 비밀번호 일치 여부도 hash 값으로 해야함
        if (!user.getPassword().equals(password)) throw new CustomException(CommonCode.WRONG_PASSWORD);
    }


    public List<DevUser> findAllUserByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.find(query, DevUser.class);
    }

    public DevUser findUserById(String id) {
        Optional<DevUser> targetUser = devUserRepository.findById(id);
        if (targetUser.isEmpty()) {
            throw new RuntimeException("해당 ID의 사용자가 없습니다");
        }
        return targetUser.get();

    }

    public List<DevUser> findAll() {
        return devUserRepository.findAll();
    }

    public UpdateResult updatePw(ChangePwDto user) {
//        mongoTemplate.insert(user, DevUser.class);
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("email").is(user.getEmail()));

        update.set("password", user.getNewPassword());
        return mongoTemplate.updateFirst(query, update, DevUser.class);
    }

//    public void join(UserJoinDto userJoinDto){
//        //Document 생성(insert)
//        //mongoDB에 DevUser라는 컬렌션에 사용자가 입력한 값 입력
//        //devUserRepository.insert(userJoinDto);
//        mongoTemplate.insert(userJoinDto, COLLECTION_NAME);
//    }

}
