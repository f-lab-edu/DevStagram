package com.moondysmell.devstausers.service;

import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.repository.DevUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DevUserService {
    private final DevUserRepository devUserRepository;
    private final MongoTemplate mongoTemplate;

    public DevUser findUserByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        DevUser targetUser = mongoTemplate.findOne(query, DevUser.class);
        return targetUser;
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
}
