package com.project.mhnbackend.freeBoard.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "freeBoard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ElementCollection
    @Builder.Default
    private List<BoardImage> imageList = new ArrayList<>();

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public void addImage(BoardImage image) {
        image.changeOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName) {
        BoardImage boardImage = BoardImage.builder().fileName(fileName).build();
        addImage(boardImage);
    }

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
    
    
    public void changeTitleAndContent(String title, String content) {
    	this.title = title;
    	this.content= content;
    	this.updateDate = LocalDateTime.now();
    }
}