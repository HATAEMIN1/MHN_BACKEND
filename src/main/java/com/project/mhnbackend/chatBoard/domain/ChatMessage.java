package com.project.mhnbackend.chatBoard.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {
    @Id
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private ZonedDateTime createdAt;
}
