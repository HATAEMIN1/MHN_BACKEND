//package com.project.mhnbackend.chatBoard.controller;
//
//import com.project.mhnbackend.chatBoard.controller.request.JoinRoomRequest;
//import com.project.mhnbackend.chatBoard.domain.ChatMessage;
//import com.project.mhnbackend.chatBoard.domain.ChatRoom;
//import com.project.mhnbackend.chatBoard.dto.ChatRoomDTO;
//import com.project.mhnbackend.chatBoard.service.ChatMessageService;
//import com.project.mhnbackend.chatBoard.service.ChatRoomService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequiredArgsConstructor
//public class ChatController { //handles web requests and websocket communications
//    private final SimpMessagingTemplate simpMessagingTemplate;
//    private final ChatMessageService chatMessageService; //creates chatrooms
//    private final ChatRoomService chatRoomService; //saves messages
//
//    @MessageMapping("/private-message")
//    public ChatMessage receiveMessage(@Payload ChatMessage chatMessage) {
//        // Associate the message with a chat room
//        Optional<String> chatRoomId = chatRoomService.getChatRoomId(
//                chatMessage.getSenderId(),
//                chatMessage.getRecipientId(),
//                true // create a new room if it doesn't exist
//        );
//
//        chatRoomId.ifPresent(chatMessage::setChatRoomId);
//
//        ChatMessage savedMsg = chatMessageService.saveMessage(chatMessage);
//        simpMessagingTemplate.convertAndSendToUser(
//                String.valueOf(chatMessage.getRecipientId()),
//                "/private",
//                chatMessage
//        );
//        System.out.println(savedMsg.toString());
//        return savedMsg;
//    }
//
//    @MessageMapping("/chat.joinRoom")
//    @SendTo("/topic/public")
//    public ChatRoomDTO joinRoom(@Payload JoinRoomRequest joinRequest) {
//        String chatRoomId = chatRoomService.getChatRoomId(
//                joinRequest.getSenderId(),
//                joinRequest.getRecipientId(),
//                true
//        ).orElseThrow(() -> new RuntimeException("Could not create or find chat room"));
//
//        List<ChatMessage> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
//        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
//        return new ChatRoomDTO(chatRoom, messages);
//    }
//
//    @GetMapping("/api/chat/room/{chatRoomId}")
//    public ResponseEntity<ChatRoomDTO> getChatRoomDTO(@PathVariable String chatRoomId) {
//        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
//        if (chatRoom == null) {
//            return ResponseEntity.notFound().build();
//        }
//        List<ChatMessage> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
//        return ResponseEntity.ok(new ChatRoomDTO(chatRoom, messages));
//    }
//}
