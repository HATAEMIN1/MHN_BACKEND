package com.project.mhnbackend.freeBoard.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.mhnbackend.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "free_board_comment")
public class FreeBoardComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 150, nullable = false)
	private String content;

	@JoinColumn(name = "member_id")
	@ManyToOne
	private Member member;

	@JoinColumn(name = "freeBoard_id", nullable = false)
	@ManyToOne
	private FreeBoard freeBoard;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private FreeBoardComment parent; // 부모 댓글

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<FreeBoardComment> replies = new ArrayList<>();

	private int step; // 댓글 순서
	private int depth; // 댓글 깊이
	private int level; // 댓글 레벨

	private LocalDateTime createDate;

	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}