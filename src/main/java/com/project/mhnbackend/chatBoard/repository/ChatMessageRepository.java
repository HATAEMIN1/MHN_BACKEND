package com.project.mhnbackend.chatBoard.repository;

import com.project.mhnbackend.chatBoard.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatId(String chatId);
}
