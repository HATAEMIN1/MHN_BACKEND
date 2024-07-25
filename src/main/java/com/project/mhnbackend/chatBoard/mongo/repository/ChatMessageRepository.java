package com.project.mhnbackend.chatBoard.mongo.repository;

import com.project.mhnbackend.chatBoard.mongo.dto.ChatMessageDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessageDTO, String> {
}
