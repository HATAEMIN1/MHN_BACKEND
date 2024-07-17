package com.project.mhnbackend.chatBoard.api;

import com.project.mhnbackend.chatBoard.model.ChatMessage;
import com.project.mhnbackend.chatBoard.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatApi {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/private-message")
    public ChatMessage receiveMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getRecipientId()), "/private", chatMessage);
        System.out.println(savedMsg.toString());
        return savedMsg;
    }
}
