package com.project.mhnbackend.chatBoard.dto;

import com.project.mhnbackend.chatBoardMongo.domain.ChatMessage;
import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private ChatRoom chatRoom;
    private List<ChatMessage> messages;
}
