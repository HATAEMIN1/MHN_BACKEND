package com.project.mhnbackend.freeBoard.domain;

import java.time.LocalDateTime;

import com.project.mhnbackend.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name="likes_uk",
						columnNames = {"freeBoardId", "memberId"}
				)
		}
)
public class FreeBoardLikes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name="member_id")
	@ManyToOne
	private Member member;
	
	@JoinColumn(name="freeBoard_id")
	@ManyToOne
	private FreeBoard freeBoard;
	
	private LocalDateTime createDate;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
