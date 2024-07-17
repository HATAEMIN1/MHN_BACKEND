package com.project.mhnbackend.member.service;

import com.project.mhnbackend.member.api.request.CreateAndEditMemberRequest;
import com.project.mhnbackend.member.api.response.MemberView;
import com.project.mhnbackend.member.model.MemberEntity;
import com.project.mhnbackend.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberEntity createMember(CreateAndEditMemberRequest request) {
//		MemberEntity member = new MemberEntity(request.getEmail(), ...);
        MemberEntity member = MemberEntity.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .nickName(request.getNickName())
                .password(request.getPassword())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void editMember(Long memberId, CreateAndEditMemberRequest request) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("member does not exist"));
        member.changeFullName(request.getFullName());
        member.changeNickName(request.getNickName());
        member.changePassword(request.getPassword());
        member.changeUpdatedAt(ZonedDateTime.now());
        memberRepository.save(member);
    }

    public void deleteMember(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("member does not exist"));
        memberRepository.delete(member);
    }

    public List<MemberView> getAllMembers() {
        List<MemberEntity> members = memberRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return members.stream().map(member -> MemberView.builder()
                .id(member.getId()).email(member.getEmail())
                .fullName(member.getFullName()).nickName(member.getNickName())
                .password(member.getPassword()).createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build()).collect(Collectors.toList());
    }

    public MemberView getMember(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("member does not exist"));
        return MemberView.builder()
                .id(member.getId()).email(member.getEmail())
                .fullName(member.getFullName()).nickName(member.getNickName())
                .password(member.getPassword()).createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
