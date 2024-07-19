package com.project.mhnbackend.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.mhnbackend.member.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{

	@EntityGraph(attributePaths = "memberTypeList")
	@Query("select m from Member m where m.email = :email")
	Member getWithRole(@Param("email")String email);
}
