package com.moondysmell.devstausers.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moondysmell.devstausers.domain.document.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
