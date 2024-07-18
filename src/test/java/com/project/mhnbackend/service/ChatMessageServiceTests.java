package com.project.mhnbackend.service;

import com.project.mhnbackend.chatBoard.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.service.ChatMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;

import java.time.ZonedDateTime;

@SpringBootTest
public class ChatMessageServiceTests {
    @Autowired
    private ChatMessageService chatMessageService;

    @Test
    public void testSaveMessage() {
        ChatMessage message = ChatMessage.builder()
                .senderId(1L)
                .recipientId(2L)
                .content("hi")
                .createdAt(ZonedDateTime.now())
                .build();
        System.out.println("ChatRoom ID: " + message.getChatRoomId());
        ChatMessage savedMessage = chatMessageService.saveMessage(message);
        assertNotNull(savedMessage);
        assertEquals(message.getContent(), savedMessage.getContent());

    }

    @Test
    public void testGetMessagesByChatRoomId() {

    }
}
