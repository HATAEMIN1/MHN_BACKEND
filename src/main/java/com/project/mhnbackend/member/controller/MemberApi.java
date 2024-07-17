package com.project.mhnbackend.member.controller;

import com.project.mhnbackend.member.controller.request.CreateAndEditMemberRequest;
import com.project.mhnbackend.member.controller.response.MemberView;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberService memberService;

    @CrossOrigin
    @GetMapping("/members")
    public List<MemberView> getMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/member/{memberId}")
    public MemberView getMember(@PathVariable("memberId") Long memberId) {
        return memberService.getMember(memberId);
    }

    @PostMapping("/member")
    public Member createMember(@RequestBody CreateAndEditMemberRequest request) {
        return memberService.createMember(request);
    }

    @PutMapping("/member/{memberId}")
    public void editMember(@PathVariable("memberId") Long memberId, @RequestBody CreateAndEditMemberRequest request) {
        memberService.editMember(memberId, request);
    }

    @DeleteMapping("/member/{memberId}")
    public void deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
    }
}
