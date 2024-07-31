package com.project.mhnbackend.member.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.project.mhnbackend.member.domain.MemberType;
import com.project.mhnbackend.subscription.domain.Subscription;
import com.project.mhnbackend.subscription.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mhnbackend.common.exception.ex.CustomException;
import com.project.mhnbackend.common.util.JWTUtil;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.dto.request.LoginRequestDTO;
import com.project.mhnbackend.member.dto.request.SignUpRequestDTO;
import com.project.mhnbackend.member.dto.response.MemberEditResponseDTO;
import com.project.mhnbackend.member.repository.MemberRepository;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegisterMail registerMail;


    @Value("${com.study.spring.upload.path}")
    private String uploadPath;
    
   
    
    private Map<String, SignUpRequestDTO> tempMembers = new ConcurrentHashMap<>();
    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public boolean isEmailExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExists(String nickName) {
        return memberRepository.findByNickName(nickName).isPresent();
    }

//    public void registerMember(SignUpRequestDTO signUpRequestDTO) {
//    	
//        if (isEmailExists(signUpRequestDTO.getEmail())) {
//            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
//        }
//
//        if (isEmailExists(signUpRequestDTO.getNickName())) {
//            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
//        }
//
//        Member member = Member.builder()
//                .email(signUpRequestDTO.getEmail())
//                .password(passwordEncoder.encode(signUpRequestDTO.getPassword())) 
//                .nickName(signUpRequestDTO.getNickName())
//                .build();
//
//        memberRepository.save(member);
//    }
//    public void registerMember(SignUpRequestDTO signUpRequestDTO) throws Exception {
//        if (isEmailExists(signUpRequestDTO.getEmail())) {
//            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
//        }
//
//        if (isNicknameExists(signUpRequestDTO.getNickName())) {
//            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
//        }
//
//        // 이메일 인증 코드 생성 및 전송
//        String ePw = registerMail.sendSimpleMessage(signUpRequestDTO.getEmail());
//        verificationCodes.put(signUpRequestDTO.getEmail(), ePw);
//        tempMembers.put(signUpRequestDTO.getEmail(), signUpRequestDTO);
//    }
    public void registerMember(SignUpRequestDTO signUpRequestDTO) throws Exception {
        String email = signUpRequestDTO.getEmail();

        if (!verifiedEmails.getOrDefault(email, false)) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }

        if (isEmailExists(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (isNicknameExists(signUpRequestDTO.getNickName())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .nickName(signUpRequestDTO.getNickName())
                .build();
        member.addType(MemberType.USER);
        memberRepository.save(member);
        // Clean up temporary storage
        verifiedEmails.remove(email);
    }

    public void confirmRegistration(String email, String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            SignUpRequestDTO signUpRequestDTO = tempMembers.get(email);
            if (signUpRequestDTO != null) {
                Member member = Member.builder()
                        .email(signUpRequestDTO.getEmail())
                        .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                        .nickName(signUpRequestDTO.getNickName())
                        .build();
                memberRepository.save(member);

                // Clean up temporary storage
                tempMembers.remove(email);
                verificationCodes.remove(email);
            } else {
                throw new IllegalArgumentException("회원가입 요청을 찾을 수 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("잘못된 인증 코드입니다.");
        }
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
    public boolean isEmailVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }
    public void confirmEmail(String email, String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            verifiedEmails.put(email, true); // 이메일 인증 완료 표시
            verificationCodes.remove(email); // 인증 코드는 제거
        } else {
            throw new IllegalArgumentException("잘못된 인증 코드입니다.");
        }
    }
    private Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();
    
    public void sendVerificationEmail(String email) throws Exception {
        // 이메일 인증 코드 생성 및 전송
        String ePw = registerMail.sendSimpleMessage(email);
        verificationCodes.put(email, ePw);
    }

//    public String login(LoginRequestDTO loginRequestDTO) {
//        Optional<Member> optionalMember = memberRepository.findByEmail(loginRequestDTO.getEmail());
//
//        if (optionalMember.isPresent()) {
//            Member member = optionalMember.get();
//            if (passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
//                Map<String, Object> claims = new HashMap<>();
//                claims.put("email", member.getEmail());
//                claims.put("nickName", member.getNickName());
//                // 필요한 추가 정보도 claims에 넣을 수 있습니다.
//                return JWTUtil.generateToken(claims, 60); // 토큰 유효기간을 60분으로 설정
//            }
//        }
//        throw new RuntimeException("Invalid login credentials");
//    }
    public void updateNickName(Long id, String nickName) {
        if (nickName.length() < 2 || nickName.length() > 8) {
            throw new CustomException("닉네임은 2글자 이상 8글자 이하로 입력해주세요.");
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException("Member not found for this id :: " + id));

        if (isNicknameExists(nickName)) {
            throw new CustomException("이미 존재하는 닉네임입니다.");
        }

        member.changeNickName(nickName);
        memberRepository.save(member);
    }

    public void updatePassword(Long id, String password) {
        String passwordPattern = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new CustomException("비밀번호는 특수문자가 포함된 8글자 이상이어야 합니다.");
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException("Member not found for this id :: " + id));

        member.changePassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    public void updateProfileImageUrl(Long id, String profileImageUrl) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException("Member not found for this id :: " + id));

        // 기존 프로필 이미지가 있으면 삭제
        String existingProfileImageUrl = member.getProfileImageUrl();
        if (existingProfileImageUrl != null && !existingProfileImageUrl.isEmpty()) {
            deleteFile(existingProfileImageUrl);
        }

        member.changeProfileImageUrl(profileImageUrl);
        memberRepository.save(member);
    }

    private void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("=") + 1);
        Path filePath = Paths.get("uploadPath", fileName);
        Path thumbnailPath = Paths.get("uploadPath", "s_" + fileName);
        try {
            Files.deleteIfExists(filePath);
            Files.deleteIfExists(thumbnailPath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류가 발생했습니다.");
        }
    }
    public MemberEditResponseDTO getMemberEditResponse(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException("Member not found for this id :: " + id));
        Optional<Subscription> subscription = subscriptionRepository.findByMemberId(id);
        return new MemberEditResponseDTO(
                member.getEmail(),
                member.getNickName(),
                member.getProfileImageUrl(),
                member.getTel(),
                member.getName(),
                subscription.map(Subscription::getNextBillingDate).orElse(null)
        );
    }

}
