package com.project.mhnbackend.freeBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.mhnbackend.freeBoard.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

}
