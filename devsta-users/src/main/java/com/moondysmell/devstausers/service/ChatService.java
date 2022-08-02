package com.moondysmell.devstausers.service;

import com.moondysmell.devstausers.config.RabbitConfig;
import com.moondysmell.devstausers.domain.document.ChatMessage;
import com.moondysmell.devstausers.domain.document.ChatRoom;
import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.ChatMessageDto;
import com.moondysmell.devstausers.repository.ChatMessageRepository;
import com.moondysmell.devstausers.repository.ChatRoomRepository;

import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Service
public class ChatService {


    private final RabbitTemplate rabbitTemplate;
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 채팅방리스트
     * 관련 메소드 구현
     **/
    //채팅방 리스트
    public List<ChatRoom> findAllChatByUser(String userId) {

        //member에 내가 포함된 채팅리스트 find
        Query query = new Query(Criteria.where("members"));

        //최신 메시지 순으로 정렬 구현

        return mongoTemplate.find(query, ChatRoom.class);

    }

    //마지막 채팅메시지
    public ChatMessage getLastMessage(String roomId){
        Query query = new Query(Criteria.where("roomId").is(roomId)).with(Sort.by(Sort.Direction.DESC, "createDt"));
        return mongoTemplate.findOne(query, ChatMessage.class);
    }

    //채팅방 생성
    public ChatRoom createRoom(String receiver, String sender){
        Query query = new Query(Criteria.where("members").all(receiver, sender));

        // ChatRoom isExistChatRoomByUsers = mongoTemplate.findOne(query, ChatRoom.class);
        Boolean isExistChatRoomByUsers = mongoTemplate.exists(query, ChatRoom.class);

        //해당 유저(members)들이 가진 채팅방이 없다면 채팅방 생성
        if(!isExistChatRoomByUsers){
            List<String> members = new ArrayList<>();
            members.add(receiver);
            members.add(sender);

            ChatRoom newChatRoom = ChatRoom.builder()
                    .members(members)
                    .build();
            return roomRepository.save(newChatRoom);
        }else{// 채팅방이 있다면 채팅방 정보 출력

            return mongoTemplate.findOne(query, ChatRoom.class);
        }

    }

    //채팅방 삭제
    public void deleteChatRoom(String roomId) {
        roomRepository.deleteById(roomId);
    }

    /**
     * 채팅메시지
     * 관련 메소드 구현
     **/
    //채팅 메시지 저장
    public ChatMessage saveMessage(ChatMessageDto chatMessageDto) {
        //채팅 메시지가 하나라도 전달되면 채팅방 생성

        //채팅 메시지 db에 저장
        ChatMessage saveMessage = ChatMessage.builder()
                .roomId(chatMessageDto.getRoomId())
                .sender(chatMessageDto.getSender())
                .receiver(chatMessageDto.getReceiver())
                .message(chatMessageDto.getMessage())
                .createDt(LocalDateTime.now())
                .build();

        saveMessage = messageRepository.save(saveMessage);
        if(saveMessage.getId() != null){
            rabbitTemplate.convertAndSend(RabbitConfig.CHAT_EXCHANGE_NAME, "room."+ chatMessageDto.getRoomId(), saveMessage);
            //CHAT_EXCHANGE_NAME(amp.topic)을 구독하는 클라이언트에게 해당 메시지 전송(addQueue)
        }

        return saveMessage;
    }

    //체팅 메시지 가져오기
    public List<ChatMessage> getMessagesByUser(ObjectId roomId){
        Query query = new Query(Criteria.where("roomId").is(roomId)).with(Sort.by(Sort.Direction.ASC, "creatDt"));
        List<ChatMessage> chatMessages = mongoTemplate.find(query, ChatMessage.class);
        return chatMessages;
    }
}
