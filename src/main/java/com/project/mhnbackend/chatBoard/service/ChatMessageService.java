//package com.project.mhnbackend.chatBoard.service;
//
//import com.project.mhnbackend.chatBoard.domain.ChatMessage;
//import com.project.mhnbackend.chatBoard.repository.ChatMessageRepository;
//import com.project.mhnbackend.chatBoard.repository.ChatRoomRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ChatMessageService {
//    private final ChatMessageRepository chatMessageRepository;
//    private final ChatRoomService chatRoomService;
//
//    @Transactional
//    public ChatMessage saveMessage(ChatMessage chatMessage) {
//        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true).orElseThrow(() -> new RuntimeException("Could not create or find chat room"));
//        chatMessage.setChatRoomId(chatId);
//        chatMessageRepository.save(chatMessage);
//        return chatMessage;
//    }
//
//    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
//        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
//        return chatId.map(chatMessageRepository::findByChatRoomId).orElse(new ArrayList<>());
//    }
//
//    public List<ChatMessage> getMessagesByChatRoomId(String chatRoomId) {
//        return chatMessageRepository.findByChatRoomId(chatRoomId);
//    }
//}
