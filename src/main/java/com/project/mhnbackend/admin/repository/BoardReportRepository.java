package com.project.mhnbackend.admin.repository;

import com.project.mhnbackend.admin.domain.BoardReport;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardReportRepository extends JpaRepository<BoardReport,Long> {
	boolean existsByMemberAndFreeBoard(Member member, FreeBoard freeBoard);
}
