package com.project.mhnbackend.member.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.mhnbackend.common.util.JWTUtil;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.dto.request.LoginRequestDTO;
import com.project.mhnbackend.member.dto.request.SignUpRequestDTO;
import com.project.mhnbackend.member.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isEmailExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExists(String nickName) {
        return memberRepository.findByNickName(nickName).isPresent();
    }

    public void registerMember(SignUpRequestDTO signUpRequestDTO) {
    	
        if (isEmailExists(signUpRequestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (isEmailExists(signUpRequestDTO.getNickName())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword())) 
                .nickName(signUpRequestDTO.getNickName())
                .build();

        memberRepository.save(member);
    }
    public String login(LoginRequestDTO loginRequestDTO) {
        Optional<Member> optionalMember = memberRepository.findByEmail(loginRequestDTO.getEmail());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("email", member.getEmail());
                claims.put("nickName", member.getNickName());
                // 필요한 추가 정보도 claims에 넣을 수 있습니다.
                return JWTUtil.generateToken(claims, 60); // 토큰 유효기간을 60분으로 설정
            }
        }
        throw new RuntimeException("Invalid login credentials");
    }
}
