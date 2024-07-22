package com.project.mhnbackend.chatBoard.mongo.service;

import com.project.mhnbackend.chatBoard.mongo.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.mongo.dto.ChatMessageDTO;
import com.project.mhnbackend.chatBoard.mongo.repository.ChatMessageRepository;
import com.project.mhnbackend.chatBoard.service.ChatRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

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
