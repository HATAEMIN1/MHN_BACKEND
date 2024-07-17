package com.project.mhnbackend.freeBoard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "board")
@Table(name = "board_image")
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String fileName;

    @Builder.Default
    private int ord = 0; // 기본값을 0으로 설정

    private LocalDateTime createdAt;
}
