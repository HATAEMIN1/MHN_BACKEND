package com.project.mhnbackend.freeBoard.domain;

import com.project.mhnbackend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "freeBoard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"board", "member"})
public class FreeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
    
    private LocalDateTime createdAt;
}
