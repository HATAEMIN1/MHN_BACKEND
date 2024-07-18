package com.project.mhnbackend.chatBoardMongo.repository;

import com.project.mhnbackend.chatBoardMongo.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(String chatRoomId);
}
