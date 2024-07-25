package com.project.mhnbackend.chatBoard.service;

import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import com.project.mhnbackend.chatBoard.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public String getChatRoomId(Long senderId, Long recipientId) { // creates chatroom and returns the chatroom id if it doesn't exist
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        ChatRoom chatRoom = null;
        if (!chatRooms.isEmpty()) {
            chatRoom = chatRooms.get(0);
            log.info("chat room id retrieved:" + chatRoom.getChatRoomId());
            if (chatRooms.size() > 1) {
                chatRooms.clear();
                chatRooms.add(chatRoom);
            }
        }
        if (chatRoom!=null) {
            return chatRoom.getChatRoomId();
        } else {
            String chatRoomId = String.format("%s_%s_%s", senderId+"", recipientId+"", UUID.randomUUID().toString());
            ChatRoom senderRecipient = ChatRoom.builder()
                    .chatRoomId(chatRoomId) // chatId is the chatroom id
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build();
            ChatRoom recipientSender = ChatRoom.builder()
                    .chatRoomId(chatRoomId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build();
            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);
            return chatRoomId;
        }
    }

    public ChatRoom getChatRoomByChatRoomId(String chatRoomId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        for (ChatRoom chatRoom : chatRoomList) {
            if (chatRoom.getChatRoomId().equals(chatRoomId)) return chatRoom;
        }
        return null;
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}
