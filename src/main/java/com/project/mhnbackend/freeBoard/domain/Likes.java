//package com.project.mhnbackend.freeBoard.domain;
//
//import java.time.LocalDateTime;
//
////import com.project.mhnbackend.user.domain.Member;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Builder
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(name = "likes_uk", columnNames = {"boardId", "memberId"})})
//public class Likes {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board board;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)
////    private Member member;
//
//    private LocalDateTime createDate;
//
//    @PrePersist
//    public void createDate() {
//        this.createDate = LocalDateTime.now();
//    }
//}
