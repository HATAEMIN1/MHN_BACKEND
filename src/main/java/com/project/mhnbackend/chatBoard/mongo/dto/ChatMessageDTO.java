package com.project.mhnbackend.chatBoard.mongo.dto;

import com.project.mhnbackend.chatBoard.mongo.domain.ChatMessage;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessageDTO {
    @Id
    private String chatRoomId;
    @Builder.Default
    List<ChatMessage> messages = new ArrayList<>();
}
