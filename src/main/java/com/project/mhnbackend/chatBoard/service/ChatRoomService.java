package com.project.mhnbackend.chatBoard.service;

import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import com.project.mhnbackend.chatBoardMongo.repository.ChatMessageRepository;
import com.project.mhnbackend.chatBoard.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(()->{
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatRoomId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatRoomId(Long senderId, Long recipientId) {
        String chatId = String.format("%s_%s", senderId+"", recipientId+"");
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId) // chatId is the chatroom id
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }

    public ChatRoom getChatRoomById(String chatRoomId) {
        return chatRoomRepository.findByChatRoomId(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room not found with id: " + chatRoomId));
    }

    public ChatRoom getChatRoomOrCreate(Long senderId, Long recipientId) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .orElseGet(() -> createChatRoom(senderId, recipientId));
    }

    private ChatRoom createChatRoom(Long senderId, Long recipientId) {
        String chatId = String.format("%s_%s", senderId, recipientId);
        ChatRoom chatRoom = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
        return chatRoomRepository.save(chatRoom);
    }
}
