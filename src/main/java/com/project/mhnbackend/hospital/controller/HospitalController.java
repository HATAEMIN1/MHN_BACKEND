package com.project.mhnbackend.hospital.controller;

import com.project.mhnbackend.hospital.domain.HospitalBMK;
import com.project.mhnbackend.hospital.domain.HospitalComment;
import com.project.mhnbackend.hospital.dto.request.HospitalBMKRequestDTO;
import com.project.mhnbackend.hospital.dto.request.HospitalCommentRequestDTO;
import com.project.mhnbackend.hospital.dto.response.*;
import com.project.mhnbackend.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class HospitalController {
	private final HospitalService hospitalService;
	
	
	//=== gpt버전 3km이내 반경 병원 겟===
	@GetMapping("/hospitals")
	public List<HospitalResponseDTO> getHospitalsInArea(
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude) {
		return hospitalService.getHospitalsInArea(latitude, longitude);
	}
	
	// 병원 상세정보 겟
	@GetMapping("/hospitals/view")
	public HospitalResponseDTO getHospitalView(
			@RequestParam("id") Long id         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
	) {
		return hospitalService.getHospitalView(id);
	}
	// 병원 후기 리뷰 등록
	@PostMapping("/hospitals/review")
	public HospitalComment createHospitalComment(
			@RequestBody HospitalCommentRequestDTO hospitalCommentRequestDTO
	){
		hospitalService.createHospitalComment(hospitalCommentRequestDTO);
		return null;
	}
	
	// 병원 후기 리뷰 리스트 겟
	@GetMapping("/hospitals/review")
	public List<HospitalCommentResponseDTO> getHospitalComments(
			@RequestParam("hospitalId") Long hospitalId
	){
//		return hospitalService.getHospitalAllComment();
		return hospitalService.getHospitalAllComment(hospitalId);
	}
	
	// 병원 후기 리뷰 삭제
	@DeleteMapping("/hospitals/review")
	public String deleteHospitalComment(
			@RequestParam("id") Long id
	) {
		return hospitalService.deleteHospitalComment(id);
	}
	
	
	// 병원 북마크 등록
	@PostMapping("/hospitals/bmk")
	public HospitalBMK createHospitalBMK(
			@RequestBody HospitalBMKRequestDTO hospitalBMKRequestDTO
	){
		return hospitalService.createHospitalBMK(hospitalBMKRequestDTO);
	}
	
	// 병원 북마크 상태 겟
	@GetMapping("/hospitals/bmk")
	public HospitalBMKResponseDTO getHospitalBMK(
			@RequestParam("hospitalId") Long hospitalId,         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
			@RequestParam("memberId") Long memberId         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
	) {
		return hospitalService.getHospitalBMK(hospitalId,memberId);
	}
	
	// 병원 북마크 해제
	@DeleteMapping("/hospitals/bmk")
//	public HospitalBMKResponseDTO deleteHospitalBMK(
	public String deleteHospitalBMK(
//			@RequestParam("hospitalId") Long hospitalId,         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
//			@RequestParam("memberId") Long memberId         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
			@RequestParam("id") Long id
	) {
		return hospitalService.deleteHospitalBMK(id);
	}
	
	
	// 병원 북마크 총 개수 카운트
	@GetMapping("/hospitals/bmk/count")
	public HospitalBMKCountResponseDTO getHospitalBMKCount(
			@RequestParam("hospitalId") Long hospitalId
	){
		return hospitalService.getHospitalBMKCount(hospitalId);
	}
	
	
// 병원 별점 평균 겟
	@GetMapping("/hospitals/review/rating")
	public HospitalRatingAVGResponseDTO getHospitalRatingAVG(
			@RequestParam("hospitalId") Long hospitalId
	){
		return hospitalService.getHospitalRatingAVG(hospitalId);
	}
	
	
	// 즐겨찾기 한 병원리스트 겟
	@GetMapping("/hospitals/account")
	public List <HospitalAccountBMKListResponseDTO> getHospitalAccountBMKList(
//			@RequestParam("hospitalId") Long hospitalId,         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
			@RequestParam("memberId") Long memberId         // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
	){
//	return hospitalService.getHospitalAccountBMKList(hospitalId,memberId);
	return hospitalService.getHospitalAccountBMKList(memberId);
	}
	
	
	// 검색결과 리스트 겟
		@GetMapping("hospitals/search")
	public List<HospitalResponseDTO> getSearchedHospitalListByName(
			@RequestParam("name") String name
	){
		return hospitalService.getSearchedHospitalListByName(name);
	}
}