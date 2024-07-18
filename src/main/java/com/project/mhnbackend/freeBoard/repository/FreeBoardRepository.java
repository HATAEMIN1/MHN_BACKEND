package com.project.mhnbackend.freeBoard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    @Query("SELECT f FROM FreeBoard f")
    Page<FreeBoard> pageFreeBoard(Pageable pageable);
}
