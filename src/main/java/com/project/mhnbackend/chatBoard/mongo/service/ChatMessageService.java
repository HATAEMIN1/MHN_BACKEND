package com.project.mhnbackend.chatBoard.mongo.service;

import com.project.mhnbackend.chatBoard.mongo.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.mongo.dto.ChatMessageDTO;
import com.project.mhnbackend.chatBoard.mongo.repository.ChatMessageRepository;
import com.project.mhnbackend.chatBoard.service.ChatRoomService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    // do saveMessages with the chatRoomId here
    // assume each message has chatRoomId already
    //private String chatRoomId;
    //    private Long senderId;
    //    private Long recipientId;
    @Transactional
    public ChatMessageDTO saveMessages(ChatMessageDTO chatMessageDTO) {
        Optional<ChatMessageDTO> existingDTO = chatMessageRepository.findById(chatMessageDTO.getChatRoomId());

        if (existingDTO.isPresent()) {
            // If it exists, update the messages
            ChatMessageDTO updatedDTO = existingDTO.get();
            updatedDTO.getMessages().addAll(chatMessageDTO.getMessages());
            ChatMessageDTO savedDTO = chatMessageRepository.save(updatedDTO);
            if (savedDTO.getMessages() == null || savedDTO.getMessages().isEmpty()) {
                log.error("ExistingDTO present and SavedDTO messages is null or empty");
            } else {
                log.info("Successfully saved chatMessageDTO, there was an existingDTO: {}", savedDTO);
            }
            return savedDTO;
        } else {
            // If it doesn't exist, save the new DTO
            ChatMessageDTO savedDTO = chatMessageRepository.save(chatMessageDTO);
            if (savedDTO.getMessages() == null || savedDTO.getMessages().isEmpty()) {
                log.error("ExistingDTO not present and SavedDTO messages is null or empty");
            } else {
                log.info("Successfully saved chatMessageDTO, there was no previous existing chatMessageDTO: {}", savedDTO);
            }
            return savedDTO;
        }
    }

    @Transactional
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        log.info("inside chat service");
        log.info("Saving message: {}", chatMessage);
        String chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId());
        chatMessage.setChatRoomId(chatId);
        // Find the ChatMessageDTO for the given chatRoomId or create a new one if not found
        Optional<ChatMessageDTO> optionalChatMessageDTO = chatMessageRepository.findById(chatId);
        ChatMessageDTO chatMessageDTO;

        if (optionalChatMessageDTO.isPresent()) {
            chatMessageDTO = optionalChatMessageDTO.get();
        } else {
            chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setChatRoomId(chatId);
        }

        chatMessageDTO.getMessages().add(chatMessage);
        chatMessageRepository.save(chatMessageDTO);

        log.info("Saved message: {}", chatMessage);
        log.info("Exiting chat service");
        return chatMessage;
    }

    public List<ChatMessage> getMessagesByChatRoomId(String chatRoomId) {
        List<ChatMessageDTO> dtos = chatMessageRepository.findAll();
        for (ChatMessageDTO dto : dtos) {
            if (dto.getChatRoomId().equals(chatRoomId)) {
                return dto.getMessages();
            }
        }
        return new ArrayList<>();
    }
}
