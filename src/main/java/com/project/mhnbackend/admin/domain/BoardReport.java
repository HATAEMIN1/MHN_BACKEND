package com.project.mhnbackend.admin.domain;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BoardReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="free_board_id")
	private FreeBoard freeBoard;
	
	private LocalDateTime createdAt;
}
