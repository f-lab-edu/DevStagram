package com.moondysmell.devstausers.controller;

import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CommonResponse;
import com.moondysmell.devstausers.domain.document.ChatMessage;

import com.moondysmell.devstausers.domain.document.ChatRoom;
import com.moondysmell.devstausers.domain.dto.ChatMessageDto;
import com.moondysmell.devstausers.service.ChatService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    //채팅 리스트
    @GetMapping("/rooms")
    public CommonResponse getRoomsByUserId(@RequestHeader("userId") String userId){

        if(!userId.isEmpty()){
            List<ChatRoom> chatRooms = chatService.findAllChatByUser(userId);
            return new CommonResponse(CommonCode.SUCCESS, Map.of("chatRooms", chatRooms));
        }else return new CommonResponse(CommonCode.FAIL);

    }



    //채팅방 생성
    @PostMapping("/createRoom")
    public CommonResponse createRoom(@RequestHeader("receiver") String receiver, @RequestHeader("userId") String sender) {
        ChatRoom chatRoom = chatService.createRoom(receiver, sender);

        return new CommonResponse(CommonCode.SUCCESS, Map.of("chatRoom", chatRoom));
    }
    

    //채팅방 
    @GetMapping("/room/{chatRoomId}")
    public CommonResponse getMessages(@PathVariable ObjectId chatRoomId){

        //ChatMessage chatMessage = new ChatMessage();
//        model.addAttribute("chatRoomId",chatRoomId);
//        model.addAttribute("nickname", nickname);
        List<ChatMessage> messages = chatService.getMessagesByUser(chatRoomId);

        return new CommonResponse(CommonCode.SUCCESS, Map.of("messages", messages));
    }

    @PostMapping("/message")
    public CommonResponse sendMessage(@RequestBody ChatMessageDto chatMessageDto){

        ChatMessage chatMessage = chatService.saveMessage(chatMessageDto);
        return new CommonResponse(CommonCode.SUCCESS, Map.of("chatMessage", chatMessage));
    }
}
