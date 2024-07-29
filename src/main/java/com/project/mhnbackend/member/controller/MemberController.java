package com.project.mhnbackend.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.mhnbackend.member.dto.request.LoginRequestDTO;
import com.project.mhnbackend.member.dto.request.SignUpRequestDTO;
import com.project.mhnbackend.member.service.MemberService;

@RestController
@RequestMapping("/api/v1")
public class MemberController {

	@Autowired
	private MemberService memberService;

//    @PostMapping("/users/register")
//    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
//        memberService.registerMember(signUpRequestDTO);
//        return ResponseEntity.ok("회원가입 성공");
//    }

	@PostMapping("/users/register")
	public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		try {
			memberService.registerMember(signUpRequestDTO);
			return ResponseEntity.ok("회원가입 성공");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
		}
	}

	
	// 이메일 중복체크 - 필요함
	@GetMapping("/users/check-email")
	public ResponseEntity<Boolean> checkEmailExists(@RequestParam("email") String email) {
		boolean exists = memberService.isEmailExists(email);
		return ResponseEntity.ok(exists);
	}

	@GetMapping("/users/check-nickname")
	public ResponseEntity<Boolean> checkNicknameExists(@RequestParam("nickName") String nickName) {
		boolean exists = memberService.isNicknameExists(nickName);
		return ResponseEntity.ok(exists);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	}

	@PostMapping("/users/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		String token = memberService.login(loginRequestDTO);
		return ResponseEntity.ok(token);
	}

//    @PostMapping("/register")
//    public ResponseEntity<String> signin(@RequestBody SignUpRequestDTO signUpRequestDTO) {
//        try {
//            memberService.registerMember(signUpRequestDTO);
//            return ResponseEntity.ok("회원가입 요청이 접수되었습니다. 이메일을 확인하여 인증을 완료하세요.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
//        }
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
//        try {
//            memberService.confirmRegistration(email, code);
//            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 인증 중 오류가 발생했습니다.");
//        }
//    }
	
	// 회원가입 - 필요함
	@PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            if (!memberService.isEmailVerified(signUpRequestDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일 인증이 필요합니다.");
            }
            memberService.registerMember(signUpRequestDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
        }
	}

	// 인증번호로 인증하는 api - 필요함
	@PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        try {
            memberService.confirmEmail(email, code);
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 인증 중 오류가 발생했습니다.");
        }
    }

	// 이메일을 보내는 api - 필요함
	@PostMapping("/sendemail")
	public ResponseEntity<String> sendVerificationEmail(@RequestParam("email") String email) {
		try {
			if (memberService.isEmailExists(email)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
			}


			memberService.sendVerificationEmail(email);
			return ResponseEntity.ok("이메일 인증 코드가 전송되었습니다. 이메일을 확인하세요.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 인증 코드 전송 중 오류가 발생했습니다.");
		}
	}

}
