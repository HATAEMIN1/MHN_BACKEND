package com.project.mhnbackend.chatBoard.service;

import com.project.mhnbackend.chatBoard.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import com.project.mhnbackend.chatBoard.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

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
        var chatId = String.format("%s_%s", senderId+"", recipientId+"");
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

    public List<ChatMessage> getMessagesForRoom(String chatId) {
        return chatRoomRepository.findByChatId(chatId);
    }
}
