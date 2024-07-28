package com.project.mhnbackend.freeBoard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mhnbackend.freeBoard.domain.FreeBoardComment;

@Repository
public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {
	List<FreeBoardComment> findByFreeBoardIdAndParentIsNull(Long freeBoardId);

	List<FreeBoardComment> findByParentId(Long parentId);

//	@Modifying
//	@Transactional
//	@Query("DELETE FROM FreeBoardComment c WHERE c.id = :id OR c.parent.id = :id")
//	 void deleteCommentAndReplies(@Param("id") Long id);

	@Modifying
	@Transactional
	@Query("delete from FreeBoardComment f where f.parent.id = :parentId")
	void deleteRepliesByParentId(@Param("parentId") Long parentId);

	@Modifying
	@Transactional
	@Query(value = "WITH RECURSIVE comment_tree AS (" + "  SELECT id, parent_id " + "  FROM free_board_comment "
			+ "  WHERE id = :id " + "  UNION ALL " + "  SELECT c.id, c.parent_id " + "  FROM free_board_comment c "
			+ "  INNER JOIN comment_tree ct ON ct.id = c.parent_id " + ") " + "DELETE FROM free_board_comment "
			+ "WHERE id IN (SELECT id FROM comment_tree)", nativeQuery = true)
	void deleteCommentAndReplies(@Param("id") Long id);
}
