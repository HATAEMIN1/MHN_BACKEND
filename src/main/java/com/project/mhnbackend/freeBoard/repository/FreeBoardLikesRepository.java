package com.project.mhnbackend.freeBoard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.domain.FreeBoardLikes;
import com.project.mhnbackend.member.domain.Member;

@Repository
public interface FreeBoardLikesRepository extends JpaRepository<FreeBoardLikes, Long> {

	boolean existsByFreeBoardAndMember(FreeBoard freeBoard, Member member);

    // 디버깅용 메서드 추가
    @Query("SELECT COUNT(fbl) > 0 FROM FreeBoardLikes fbl WHERE fbl.freeBoard = :freeBoard AND fbl.member = :member")
    boolean existsByFreeBoardAndMemberQuery(@Param("freeBoard") FreeBoard freeBoard, @Param("member") Member member);

	Optional<FreeBoardLikes> findByFreeBoardAndMember(FreeBoard freeBoard, Member member);
}
