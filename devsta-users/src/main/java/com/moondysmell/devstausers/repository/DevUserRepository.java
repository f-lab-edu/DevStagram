package com.moondysmell.devstausers.repository;

import com.moondysmell.devstausers.domain.document.DevUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DevUserRepository extends MongoRepository<DevUser, String> {
}
