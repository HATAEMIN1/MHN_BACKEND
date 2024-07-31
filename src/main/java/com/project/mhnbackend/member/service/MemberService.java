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
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.mhnbackend.member.domain.MemberType;
import com.project.mhnbackend.subscription.domain.Subscription;
import com.project.mhnbackend.subscription.repository.SubscriptionRepository;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.mhnbackend.common.exception.ex.CustomException;
import com.project.mhnbackend.common.util.JWTUtil;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.dto.request.LoginRequestDTO;
import com.project.mhnbackend.member.dto.request.MemberDTO;
import com.project.mhnbackend.member.dto.request.SignUpRequestDTO;
import com.project.mhnbackend.member.dto.response.MemberEditResponseDTO;
import com.project.mhnbackend.member.repository.MemberRepository;

@Service
@Transactional
@Log4j2
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
                return JWTUtil.generateToken(claims, 60); 
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
            verifiedEmails.put(email, true); 
            verificationCodes.remove(email); 
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

    private MemberDTO entityToDTO(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getNickName(),
                member.getTel(),
                member.getProfileImageUrl(),
                member.getMemberTypeList().stream().map(Enum::name).collect(Collectors.toList())
        );
    }

    @Transactional
    public MemberDTO getKakaoMember(String accessToken) {
        String email = getEmailFromKakaoAccessToken(accessToken);
        log.info("Kakao email: {}", email);
        Optional<Member> result = memberRepository.findByEmail(email);

        // 기존 회원
        if (result.isPresent()) {
            return entityToDTO(result.get());
        }

        // 신규 회원 가입
        LinkedHashMap<String, String> kakaoAccount = getKakaoAccountFromAccessToken(accessToken);
        String nickname = kakaoAccount.get("nickname");
        String profileImageUrl = kakaoAccount.get("profile_image_url");

        Member socialMember = makeSocialMember(email, nickname, profileImageUrl);
        memberRepository.save(socialMember);

        return entityToDTO(socialMember);
    }

    private Member makeSocialMember(String email, String nickname, String profileImageUrl) {
        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(tempPassword))
                .nickName(nickname)
                .profileImageUrl(profileImageUrl)
                .memberTypeList(List.of(MemberType.USER))
                .build();

        return member;
    }

    private String makeTempPassword() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
        }
        return buffer.toString();
    }

    private String getEmailFromKakaoAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);

        log.info(response.toString());

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("------------------------------------");
        log.info(bodyMap.toString());

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaoAccount: " + kakaoAccount);

        return kakaoAccount.get("email");
    }

    private LinkedHashMap<String, String> getKakaoAccountFromAccessToken(String accessToken) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);

        log.info(response.toString());

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("------------------------------------");
        log.info(bodyMap.toString());

        LinkedHashMap<String, String> kakaoProfile = bodyMap.get("properties");

        log.info("kakaoProfile: " + kakaoProfile);

        return kakaoProfile;
    }
}
