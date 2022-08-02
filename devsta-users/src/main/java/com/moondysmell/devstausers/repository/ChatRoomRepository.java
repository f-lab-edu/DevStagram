package com.moondysmell.devstausers.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moondysmell.devstausers.domain.document.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

}
