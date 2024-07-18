package com.project.mhnbackend.chatBoard.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomRequest {
    private Long senderId;
    private Long recipientId;
}
