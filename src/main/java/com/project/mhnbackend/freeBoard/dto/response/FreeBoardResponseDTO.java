package com.project.mhnbackend.freeBoard.dto.response;

import java.time.LocalDateTime;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardResponseDTO {
    private String title;
    private String content;
    private String fileName;
    private LocalDateTime createDate;

}