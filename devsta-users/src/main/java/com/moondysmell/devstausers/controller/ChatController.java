package com.moondysmell.devstausers.controller;

import com.moondysmell.devstausers.common.CommonCode;
import com.moondysmell.devstausers.common.CommonResponse;
import com.moondysmell.devstausers.domain.document.ChatMessage;

import com.moondysmell.devstausers.domain.document.ChatRoom;
import com.moondysmell.devstausers.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
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
    @PostMapping
    public CommonResponse createRoom(@RequestHeader("receiver") String receiver, @RequestHeader("userId") String sender) {


        ChatRoom chatRoom = chatService.createRoom(receiver, sender);

        return new CommonResponse(CommonCode.SUCCESS, Map.of("chatRoom", chatRoom));
    }
    

    //채팅방 
    @GetMapping("/room")
    public String getMessages(String chatRoomId){

        //ChatMessage chatMessage = new ChatMessage();
//        model.addAttribute("chatRoomId",chatRoomId);
//        model.addAttribute("nickname", nickname);
        List<ChatMessage> messages = chatService.getMessagesByUser(chatRoomId);

        return "chat/room";
    }
}
