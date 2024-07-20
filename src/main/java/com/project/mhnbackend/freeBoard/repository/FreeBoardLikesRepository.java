package com.project.mhnbackend.freeBoard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.domain.FreeBoardLikes;
import com.project.mhnbackend.member.domain.Member;

public interface FreeBoardLikesRepository extends JpaRepository<FreeBoardLikes, Long> {

	boolean existsByFreeBoardAndMember(FreeBoard freeBoard, Member member);

	Optional<FreeBoardLikes> findByFreeBoardAndMember(FreeBoard freeBoard, Member member);
}
