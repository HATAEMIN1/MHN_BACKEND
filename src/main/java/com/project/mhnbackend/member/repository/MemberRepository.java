package com.project.mhnbackend.member.repository;

import com.project.mhnbackend.member.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
