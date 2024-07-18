package com.project.mhnbackend.repository;

import com.project.mhnbackend.chatBoard.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import com.project.mhnbackend.chatBoard.repository.ChatMessageRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChatMessageRepositoryTests {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Test
    public void testSave() {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId("1");
    }

}
