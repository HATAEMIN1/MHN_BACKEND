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
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class ChatMessageServiceTests {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Transactional
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

    @Transactional
    @Test
    public void testGetMessagesByChatRoomId() {
        // create and save a chat room
        String chatId = "test_chat_room";
        ChatRoom chatRoom = ChatRoom.builder()
                .chatId(chatId)
                .senderId(1L)
                .recipientId(2L)
                .build();
        chatRoomRepository.save(chatRoom);

        // then create and save some messages
        ChatMessage message1 = ChatMessage.builder()
                .chatRoomId(chatId)
                .senderId(1L)
                .recipientId(2L)
                .content("Hello")
                .createdAt(ZonedDateTime.now())
                .build();
        ChatMessage message2 = ChatMessage.builder()
                .chatRoomId(chatId)
                .senderId(2L)
                .recipientId(1L)
                .content("Hi there")
                .createdAt(ZonedDateTime.now())
                .build();
        chatMessageRepository.save(message1);
        chatMessageRepository.save(message2);

        // test getting messages by chat room id
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatId);

        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertTrue(messages.stream().anyMatch(m -> m.getContent().equals("Hello")));
        assertTrue(messages.stream().anyMatch(m -> m.getContent().equals("Hi there")));
    }
}
