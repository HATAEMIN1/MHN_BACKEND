package com.project.mhnbackend.chatBoard.repository;

import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
