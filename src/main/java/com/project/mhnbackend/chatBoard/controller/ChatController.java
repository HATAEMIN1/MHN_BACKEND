package com.project.mhnbackend.chatBoard.controller;

import com.project.mhnbackend.chatBoard.controller.request.JoinRoomRequest;
import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import com.project.mhnbackend.chatBoard.dto.ChatRoomDTO;
import com.project.mhnbackend.chatBoard.mongo.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.mongo.service.ChatMessageService;
import com.project.mhnbackend.chatBoard.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController { //handles web requests and websocket communications
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService; //creates chatrooms
    private final ChatRoomService chatRoomService; //saves messages

    @MessageMapping("/chat.sendMessage")
    public ChatMessage receiveMessage(@Payload ChatMessage chatMessage) {
        String chatRoomId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId()
        );
        chatMessage.setChatRoomId(chatRoomId);

        ChatMessage savedMsg = chatMessageService.saveMessage(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()),
                "/private",
                chatMessage
        );
        simpMessagingTemplate.convertAndSend(
                "/chatroom/" + chatRoomId,
                chatMessage
        );
        log.info("Saved message: {}", savedMsg);
        log.info("Received message: {}", chatMessage);
        log.info("Message sent to user {}", chatMessage.getRecipientId());
        return savedMsg;
    }

    @MessageMapping("/chat.joinRoom")
    public ChatRoomDTO joinRoom(@Payload JoinRoomRequest joinRequest) {
        String chatRoomId = chatRoomService.getChatRoomId(
                joinRequest.getSenderId(),
                joinRequest.getRecipientId()
        );

        List<ChatMessage> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
        ChatRoom chatRoom = chatRoomService.getChatRoomByChatRoomId(chatRoomId);
        log.info("Received join request: {}", joinRequest);
        log.info("ChatRoom Id created: {}", chatRoomId);
        return new ChatRoomDTO(chatRoom, messages);
    }

    @GetMapping("/api/chatrooms")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    @GetMapping("/api/chat/room/{chatRoomId}")
    public ResponseEntity<ChatRoomDTO> getChatRoomDTO(@PathVariable("chatRoomId") String chatRoomId) {
        log.info("Received chat room ID: {}", chatRoomId);
        ChatRoom chatRoom = chatRoomService.getChatRoomByChatRoomId(chatRoomId);
        if (chatRoom == null) {
            return ResponseEntity.notFound().build();
        }
        List<ChatMessage> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(new ChatRoomDTO(chatRoom, messages));
    }

    @GetMapping("/api/chat/room/{senderId}/{recipientId}")
    public ResponseEntity<String> getChatRoomId(@PathVariable("senderId") Long senderId, @PathVariable("recipientId") Long recipientId) {
        try {
            String chatRoomId = chatRoomService.getChatRoomId(senderId, recipientId);
            log.info("Received request for chat room with senderId: {} and recipientId: {}", senderId, recipientId);
            return ResponseEntity.ok(chatRoomId);
        } catch (Exception e) {
            log.error("Error getting chat room ID", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/chat/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable("chatRoomId") String chatRoomId) {
        log.info("Received request for messages in chat room: {}", chatRoomId);
        try {
            List<ChatMessage> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
            log.info("Returning {} messages for chat room: {}", messages.size(), chatRoomId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            log.error("Error getting messages for chat room: {}", chatRoomId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>());  // Return an empty list in case of error
        }
    }


}
