package com.project.mhnbackend.admin.controller;


import com.project.mhnbackend.admin.dto.request.BoardReportRequestDTO;
import com.project.mhnbackend.admin.dto.response.BoardReportResponseDTO;
import com.project.mhnbackend.admin.dto.response.StatsResponseForMemberDTO;
import com.project.mhnbackend.admin.dto.response.StatsResponseForSubMemberDTO;
import com.project.mhnbackend.admin.service.AdminService;
import com.project.mhnbackend.doctor.service.DoctorService;
import com.project.mhnbackend.member.service.MemberService;
import com.project.mhnbackend.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class AdminController {
	private final AdminService adminService;
	private final DoctorService doctorService;
	private final MemberService memberService;
	private final SubscriptionService subscriptionService;
	
	// 전체 유저수는 무조건 가져와야하니까..
	@GetMapping("/admin/members/list")
	public List<StatsResponseForMemberDTO> getMembers(){return memberService.getAllMemberLists();}
	
	// 수의사 리스트는
	// 모든 수의사 리스트 불러오기 get - 이거 프론트경로 똑같이해오면 됨.
//	@GetMapping("doctors/register/list")
//	public List<DoctorListResponseDTO> getAllDoctorLists() {
//		return doctorService.getAllDoctorLists();
//	}
	
	@GetMapping("/admin/submembers/list")
	public List<StatsResponseForSubMemberDTO> getSubMembers(){return subscriptionService.getAllSubMemberLists();}


	// 유저들이 신고한 게시글 리스트 전부 겟
	@GetMapping("/admin/boardreports/list")
	public List<BoardReportResponseDTO> getAllBoardReportList(){
		return adminService.getAllBoardReportList();
		
	}
}
