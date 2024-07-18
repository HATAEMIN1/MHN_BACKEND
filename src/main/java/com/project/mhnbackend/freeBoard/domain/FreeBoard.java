package com.project.mhnbackend.freeBoard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//     @ManyToOne
//     @JoinColumn(name = "member_id", nullable = true)
//     private Member member;


    private LocalDateTime createDate;
    
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

    
}
