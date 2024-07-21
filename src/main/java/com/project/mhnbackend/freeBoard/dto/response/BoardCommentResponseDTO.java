package com.project.mhnbackend.freeBoard.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentResponseDTO {
    private Long id;
    private String content;
    private String memberName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int depth;
}
