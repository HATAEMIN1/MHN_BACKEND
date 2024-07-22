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
}
