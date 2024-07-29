package com.project.mhnbackend.doctor.controller;

import com.project.mhnbackend.doctor.dto.request.DoctorSignUpRequestDTO;
import com.project.mhnbackend.doctor.dto.response.DoctorListResponseDTO;
import com.project.mhnbackend.doctor.dto.response.DoctorStatusPutResponseDTO;
import com.project.mhnbackend.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DoctorController {
	private final DoctorService doctorService;
	
	// 이메일 중복체크 - 필요함
	@GetMapping("/doctors/check-email")
	public ResponseEntity<Boolean> checkEmailExists(@RequestParam("email") String email) {
		boolean exists = doctorService.isEmailExists(email);
		return ResponseEntity.ok(exists);
	}
	
	// 이메일을 보내는 api - 필요함
	@PostMapping("/doctors/sendemail")
	public ResponseEntity<String> sendVerificationEmail(@RequestParam("email") String email) {
		try {
			if (doctorService.isEmailExists(email)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
			}
			
			
			doctorService.sendVerificationEmail(email);
			return ResponseEntity.ok("이메일 인증 코드가 전송되었습니다. 이메일을 확인하세요.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 인증 코드 전송 중 오류가 발생했습니다.");
		}
	}
	
	// 인증번호로 인증하는 api - 필요함
	@PostMapping("/doctors/verify")
	public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
		try {
			doctorService.confirmEmail(email, code);
			return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 인증 중 오류가 발생했습니다.");
		}
	}
	
	// 회원가입 - 필요함
	@PostMapping("/doctors/register")
	public ResponseEntity<String> register(@RequestBody DoctorSignUpRequestDTO doctorSignUpRequestDTO) {
		try {
			if (!doctorService.isEmailVerified(doctorSignUpRequestDTO.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일 인증이 필요합니다.");
			}
			doctorService.registerDoctor(doctorSignUpRequestDTO);
			return ResponseEntity.ok("회원가입이 완료되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
		}
	}

	
//=======================회원가입 절차 끝=======================
	
	
	// 모든 수의사 리스트 불러오기 get
	@GetMapping("doctors/register/list")
	public List<DoctorListResponseDTO> getAllDoctorLists(
	
	){
		return doctorService.getAllDoctorLists();
	}
	
	// 승인 대기 -> 승인으로 put 상태수정
	@PutMapping("/doctors/register/statusmodi")
	public DoctorStatusPutResponseDTO patchDoctorStatus(@RequestParam("id") Long id){
		return doctorService.patchDoctorStatus(id);
	}
	
	
	
	
}


