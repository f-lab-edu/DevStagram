package com.moondysmell.devstausers.repository;

import com.moondysmell.devstausers.domain.document.DevUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DevUserRepository extends MongoRepository<DevUser, String> {
//    Optional<DevUser> findByUsername(String name);
//    Boolean existsByUsername(String name);
//    Boolean existsByEmail(String email);
}
