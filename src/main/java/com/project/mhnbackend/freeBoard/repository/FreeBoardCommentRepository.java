package com.project.mhnbackend.freeBoard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.mhnbackend.freeBoard.domain.FreeBoardComment;

@Repository
public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long>{
	List<FreeBoardComment> findByFreeBoardIdAndParentIsNull(Long freeBoardId);

}
