package com.project.mhnbackend.service;

import com.project.mhnbackend.chatBoard.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import com.project.mhnbackend.chatBoard.repository.ChatMessageRepository;
import com.project.mhnbackend.chatBoard.repository.ChatRoomRepository;
import com.project.mhnbackend.chatBoard.service.ChatMessageService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Commit;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;
import java.util.Optional;


@SpringBootTest
public class ChatMessageServiceTests {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    public void testSaveMessage() {
        ChatMessage message = ChatMessage.builder()
                .id(1L)
                .senderId(1L)
                .recipientId(2L)
                .content("hi")
                .createdAt(ZonedDateTime.now())
                .build();
        String chatRoomId = String.valueOf(chatRoomRepository.findBySenderIdAndRecipientId(message.getSenderId(), message.getRecipientId())
                .map(ChatRoom::getChatId)
                .or(()->{
                    String chatId = String.format("%s_%s", message.getSenderId()+"", message.getRecipientId()+"");
                    ChatRoom senderRecipient = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(message.getSenderId())
                            .recipientId(message.getRecipientId())
                            .build();
                    ChatRoom recipientSender = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(message.getSenderId())
                            .recipientId(message.getRecipientId())
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);
                    return Optional.of(chatId);
                }));
        message.setChatRoomId(chatRoomId);
        ChatMessage savedMessage = chatMessageRepository.save(message);

        assertNotNull(savedMessage);
        assertEquals(message.getContent(), savedMessage.getContent());
    }


//    @Autowired
//    private ChatMessageService chatMessageService;
//
//    @Test
//    public void testSaveMessage() {
//        ChatMessage message = ChatMessage.builder()
//                .id(1L)
//                .senderId(1L)
//                .recipientId(2L)
//                .content("hi")
//                .createdAt(ZonedDateTime.now())
//                .build();
//        System.out.println("Chatmessage: " + String.valueOf(message));
//        ChatMessage savedMessage = chatMessageService.saveMessage(message);
//        assertNotNull(savedMessage);
//        assertEquals(message.getContent(), savedMessage.getContent());
//
//    }

    @Test
    public void testGetMessagesByChatRoomId() {

    }
}
