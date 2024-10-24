package com.project.mhnbackend.member.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.mhnbackend.common.exception.ex.CustomException;
import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.common.util.JWTUtil;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.dto.request.LoginRequestDTO;
import com.project.mhnbackend.member.dto.request.MemberDTO;
import com.project.mhnbackend.member.dto.request.SignUpRequestDTO;
import com.project.mhnbackend.member.dto.response.MemberEditResponseDTO;
import com.project.mhnbackend.member.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@RestController
@Log4j2
@RequestMapping("/api/v1")
public class MemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private FileUploadUtil fileUploadUtil;

	@Value("${com.study.spring.upload.path}")
	private String uploadPath;

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

	@PutMapping("/users/nickname")
	public ResponseEntity<String> updateNickName(@RequestParam("id") Long id,
			@RequestParam("nickName") String nickName) {
		try {
			memberService.updateNickName(id, nickName);
			return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
		} catch (CustomException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("닉네임 변경 중 오류가 발생했습니다.");
		}
	}

	@PutMapping("/users/password")
	public ResponseEntity<String> updatePassword(@RequestParam("id") Long id,
			@RequestParam("password") String password) {
		try {
			memberService.updatePassword(id, password);
			return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
		} catch (CustomException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
		}
	}

	@PostMapping("/users/profile-image")
	public ResponseEntity<String> uploadProfileImage(@RequestParam("id") Long id,
			@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("파일이 선택되지 않았습니다.");
		}

		String originalFileName = file.getOriginalFilename();
		String extension = "";
		if (originalFileName != null && originalFileName.contains(".")) {
			extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}

		// UUID로 파일 이름 생성
		String saveName = UUID.randomUUID().toString() + extension;

		// MultipartFile을 새로운 파일 이름으로 저장
		Path savePath = Paths.get(uploadPath, saveName);
		try {
			Files.copy(file.getInputStream(), savePath);
			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image")) {
				Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

				Thumbnails.of(savePath.toFile()).size(400, 400).toFile(thumbnailPath.toFile());
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
		}

		String fileUrl = "/api/v1/files?fileName=" + saveName;
		memberService.updateProfileImageUrl(id, fileUrl);

		return ResponseEntity.ok(fileUrl);
	}

	@GetMapping("/members/edit")
	public ResponseEntity<MemberEditResponseDTO> getMemberEditResponse(@RequestParam("id") Long id) {
		MemberEditResponseDTO responseDTO = memberService.getMemberEditResponse(id);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/files")
	public ResponseEntity<Resource> getFile(@RequestParam("fileName") String fileName) {
		try {
			Path filePath = Paths.get(uploadPath).resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists()) {
				throw new FileNotFoundException("File not found " + fileName);
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
					.body(resource);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/member/kakao")
	public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {
		log.info(accessToken);

		MemberDTO memberDTO = memberService.getKakaoMember(accessToken);

		Map<String, Object> claims = memberDTO.getClaims();
		String jwtAccessToken = JWTUtil.generateToken(claims, 10);
		String jwtRefreshToken = JWTUtil.generateToken(claims, 60 * 24);

		claims.put("accessToken", jwtAccessToken);
		claims.put("refreshToken", jwtRefreshToken);

		return claims;
	}
	 @PutMapping("/updateName")
	    public ResponseEntity<Member> updateMemberName(@RequestParam("memberId") Long id, @RequestParam("name") @Valid @Size(min = 2, max = 5) String name) {
	        Member updatedMember = memberService.updateName(id, name);
	        return ResponseEntity.ok(updatedMember);
	    }

	    @PutMapping("/updateTel")
	    public ResponseEntity<Member> updateMemberTel(@RequestParam("memberId") Long id, @RequestParam("tel") @Valid @Size(min = 11, max = 11) String tel) {
	        Member updatedMember = memberService.updateTel(id, tel);
	        return ResponseEntity.ok(updatedMember);
	    }
	
}
