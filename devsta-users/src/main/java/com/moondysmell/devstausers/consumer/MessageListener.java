package com.moondysmell.devstausers.consumer;

import com.moondysmell.devstausers.config.RabbitConfig;
import com.moondysmell.devstausers.domain.document.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitConfig.CHAT_QUEUE_NAME)
    public void consumeMessageFromQueue(ChatMessage message){
        System.out.println("*** Messsage : " + message.toString());
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/topic/room."+ message.getRoomId(), message);
    }
}
