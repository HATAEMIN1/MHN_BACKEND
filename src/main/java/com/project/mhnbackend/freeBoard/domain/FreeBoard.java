package com.project.mhnbackend.freeBoard.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.mhnbackend.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "freeBoard", indexes = {
	    @Index(name = "idx_title", columnList = "title")
	})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 10)
	private String title;

	@Column(nullable = false, length = 1000)
	private String content;

	@ElementCollection
	@Builder.Default
	private List<BoardImage> imageList = new ArrayList<>();

	private LocalDateTime createDate;
	private LocalDateTime updateDate;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private Member member;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FreeBoardLikes> likes = new ArrayList<>();

	@Transient
	private boolean likeState;

	@Transient
	private int likeCount;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<FreeBoardComment> comments = new ArrayList<>();

	@Transient
	private int commentCount;
	
	private static final int MAX_IMAGES = 5;
	
	 public void addImage(BoardImage image) {
	        if (this.imageList.size() >= MAX_IMAGES) {
	            throw new IllegalStateException("이미지 개수는 최대 " + MAX_IMAGES + "개까지 가능합니다.");
	        }
	        image.changeOrd(this.imageList.size());
	        imageList.add(image);
	    }

//	public void addImage(BoardImage image) {
//		image.changeOrd(this.imageList.size());
//		imageList.add(image);
//	}

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
		this.content = content;
		this.updateDate = LocalDateTime.now();
	}

	public int getCommentCount() {
		return comments.size();
	}

}