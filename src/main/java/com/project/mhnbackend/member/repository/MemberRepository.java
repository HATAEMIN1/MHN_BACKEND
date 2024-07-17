package com.project.mhnbackend.member.repository;

import com.project.mhnbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
