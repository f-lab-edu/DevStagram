package com.moondysmell.devstausers.controller;


import com.moondysmell.devstausers.domain.document.ChatMessage;
import com.moondysmell.devstausers.domain.dto.ChatMessageDto;
import com.moondysmell.devstausers.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompRabbitController {

    private final ChatService chatService;

   private final RabbitTemplate rabbitTemplate;
  // private final SimpMessageSendingOperations messagingTemplate;

   private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
   private final static String CHAT_QUEUE_NAME = "chat.queue"; //메시지를 받을 큐


   //
//    @MessageMapping("chat.enter.{chatRoomId}")
//    public void enter(ChatMessage chat, @DestinationVariable String chatRoomId){
//        chat.setMessage("입장");
//        chat.setCreateDt(LocalDateTime.now());
//
//        rabbitTemplate.convertAndSend("room." + chatRoomId, chat); //queue
//
//    }


   //publisher 메시지 발행
   @MessageMapping("chat.message.{chatRoomId}")
   public String sendMessage(@Payload ChatMessage chat, @DestinationVariable String chatRoomId){


       chat.setCreateDt(LocalDateTime.now());
       //chat.setMessage("hello");

       //messagingTemplate.convertAndSend("room."+chatRoomId, chat);
       rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);
       //rabbitTemplate.convertAndSend( "room." + chatRoomId, chat);
       //rabbitTemplate.convertAndSend("amq.topic", "room." + chatRoomId, chat);

       //처음 메시지를 보낼때 채팅방 생성
       //chatService.createRoom(chatRoomId);
       return "success!";
   }



   //리스너 구현 
   //receive()는 단순히 큐에 들어온 메세지를 소비만 한다.
   //chat.queue라는 Queue를 구독해 그 큐에 들어온 메세지를 소비하는 consumer가 되겠다라는 어노테이션.
   @RabbitListener(queues = CHAT_QUEUE_NAME)
   public void receiveChatMessage(ChatMessageDto chatMessageDto){


       log.info("***** received : " + chatMessageDto.getMessage());
       chatService.saveMessage(chatMessageDto);

   }
}
