package com.project.mhnbackend.freeBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseDTO;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

//	@Query(value = "SELECT fb.title, fb.content, fb.create_date, fbil.file_name FROM free_board fb join free_board_image_list fbil on fb.id = fbil.free_board_id ORDER BY create_date DESC", nativeQuery = true)
//	Page<Object[]> pageFreeBoard(Pageable pageable);

	@Query("SELECT fb FROM FreeBoard fb LEFT JOIN FETCH fb.imageList ORDER BY fb.createDate DESC")
	Page<FreeBoard> pageFreeBoard(Pageable pageable);

	@Query("SELECT fb FROM FreeBoard fb LEFT JOIN fb.likes l GROUP BY fb ORDER BY COUNT(l) DESC")
	Page<FreeBoard> findAllByLikesCount(Pageable pageable);

	@Query("SELECT f FROM FreeBoard f WHERE f.title LIKE %:title%")
	Page<FreeBoard> searchByTitleLike(@Param("title") String title, Pageable pageable);

	@Query("SELECT fb FROM FreeBoard fb LEFT JOIN FETCH fb.imageList ORDER BY fb.createDate ASC")
	Page<FreeBoard> pageFreeBoardByOldest(Pageable pageable);
}
