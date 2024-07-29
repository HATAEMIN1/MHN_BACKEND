package com.project.mhnbackend.chatBoard.mongo.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {
    //    @Id
    private Long id;
    @Id
    private String chatRoomId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private String createdAt;
//    @Builder.Default
//    private Instant createdAt = Instant.now();
}

