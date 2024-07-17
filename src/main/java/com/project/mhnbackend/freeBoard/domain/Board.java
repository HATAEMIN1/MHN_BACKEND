package com.project.mhnbackend.freeBoard.domain;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private LocalDateTime createdAt;

    @ElementCollection
    private List<BoardImage> imageList;

    public Board(Long id, BoardType boardType, LocalDateTime createdAt, List<BoardImage> imageList) {
        this.id = id;
        this.boardType = boardType;
        this.createdAt = createdAt;
        this.imageList = imageList != null ? imageList : new ArrayList<>();
    }

    public void addImage(BoardImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName) {
        BoardImage boardImage = BoardImage.builder().fileName(fileName).build();
        addImage(boardImage);
    }
}
