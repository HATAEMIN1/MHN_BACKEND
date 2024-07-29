package com.project.mhnbackend.doctor.service;

import com.project.mhnbackend.doctor.domain.Doctor;
import com.project.mhnbackend.doctor.dto.request.DoctorSignUpRequestDTO;
import com.project.mhnbackend.doctor.dto.response.DoctorListResponseDTO;
import com.project.mhnbackend.doctor.dto.response.DoctorLoginResponseDTO;
import com.project.mhnbackend.doctor.dto.response.DoctorStatusPutResponseDTO;
import com.project.mhnbackend.doctor.repository.DoctorRepository;
import com.project.mhnbackend.hospital.domain.Hospital;
import com.project.mhnbackend.hospital.repository.HospitalRepository;
import com.project.mhnbackend.member.dto.request.SignUpRequestDTO;
import com.project.mhnbackend.member.service.RegisterMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.project.mhnbackend.common.util.JWTUtil;

@Service
//@RequiredArgsConstructor
public class DoctorService {
	private DoctorRepository doctorRepository;
	private PasswordEncoder passwordEncoder;
	private RegisterMailForDoctor registerMailForDoctor;
	private HospitalRepository hospitalRepository;
	@Autowired
	private Optional<JWTUtil> jwtUtil;
	
	
	@Autowired
	public DoctorService(DoctorRepository doctorRepository,
	                     PasswordEncoder passwordEncoder,
	                     RegisterMailForDoctor registerMailForDoctor,
	                     HospitalRepository hospitalRepository) {
		this.doctorRepository = doctorRepository;
		this.passwordEncoder = passwordEncoder;
		this.registerMailForDoctor = registerMailForDoctor;
		this.hospitalRepository = hospitalRepository;
	}
	
	private Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();
	private Map<String, SignUpRequestDTO> tempDoctor = new ConcurrentHashMap<>();
	private Map<String, String> verificationCodes = new ConcurrentHashMap<>();
	
	public void sendVerificationEmail(String email) throws Exception {
		// 이메일 인증 코드 생성 및 전송
		String ePw = registerMailForDoctor.sendSimpleMessage(email);
		verificationCodes.put(email, ePw);
	}
	
	public boolean isEmailExists(String email) {
		return doctorRepository.findByEmail(email).isPresent();
	}
	
	public boolean isEmailVerified(String email) {
		return verifiedEmails.getOrDefault(email, false);
	}
	
	public void registerDoctor(DoctorSignUpRequestDTO doctorSignUpRequestDTO) throws Exception {
		String email = doctorSignUpRequestDTO.getEmail();
		Hospital hospital = hospitalRepository.findById(doctorSignUpRequestDTO.getHospitalId()).orElseThrow();
		
		if (!verifiedEmails.getOrDefault(email, false)) {
			throw new IllegalArgumentException("이메일 인증이 필요합니다.");
		}
		
		if (isEmailExists(email)) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
		
		Doctor doctor = Doctor.builder()
				.email(email)
				.password(passwordEncoder.encode(doctorSignUpRequestDTO.getPassword()))
				.hospital(hospital)
				.createdAt(LocalDateTime.now())
				.doctorStatus(Doctor.DoctorStatus.PENDING)
				.build();
		doctorRepository.save(doctor);
		
		// Clean up temporary storage
		verifiedEmails.remove(email);
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
	
	//=======================회원가입 절차 끝=======================
	
	//모든 수의사 리스트 겟
	public List<DoctorListResponseDTO> getAllDoctorLists() {
		List<Doctor> doctors = doctorRepository.findAll();
		return doctors.stream()
				.map(doctor -> {
					Hospital hospital = hospitalRepository.findById(doctor.getHospital().getId())
							.orElseThrow(() -> new RuntimeException("Hospital not found"));
					return DoctorListResponseDTO.builder()
							.id(doctor.getId())
							.hospital(hospital)
							.email(doctor.getEmail())
							.password(doctor.getPassword())
							.createdAt(doctor.getCreatedAt())
							.doctorStatus(doctor.getDoctorStatus())
							.build();
				})
				.collect(Collectors.toList());
	}
	
	public DoctorStatusPutResponseDTO patchDoctorStatus(Long id) {
		Optional<Doctor> doctor = doctorRepository.findById(id);
		doctor.get().setDoctorStatus(Doctor.DoctorStatus.FULFILLED);
		doctorRepository.save(doctor.get());
		return DoctorStatusPutResponseDTO.builder()
				.id(id)
				.hospital(doctor.get().getHospital())
				.email(doctor.get().getEmail())
				.password(doctor.get().getPassword())
				.createdAt(doctor.get().getCreatedAt())
				.doctorStatus(Doctor.DoctorStatus.FULFILLED)
				.build();
	}
	
//	public Map<String, String> login(String email, String password) {
//		Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);
//		if (doctorOptional.isPresent()) {
//			Doctor doctor = doctorOptional.get();
//			if (passwordEncoder.matches(password, doctor.getPassword())) {
//				Map<String, Object> claims = new HashMap<>();
//				claims.put("id", doctor.getId());
//				claims.put("email", doctor.getEmail());
//				// 필요한 다른 정보들을 추가
//
//				if (jwtUtil.isPresent()) {
//					try {
//						String accessToken = jwtUtil.get().generateToken(claims, 30);
//						String refreshToken = jwtUtil.get().generateToken(claims, 60 * 24);
//
//						Map<String, String> tokens = new HashMap<>();
//						tokens.put("accessToken", accessToken);
//						tokens.put("refreshToken", refreshToken);
//
//						return tokens;
//					} catch (Exception e) {
//						// JWT 생성 중 오류 발생 시 처리
//						throw new RuntimeException("토큰 생성 중 오류가 발생했습니다.", e);
//					}
//				} else {
//					// JWTUtil이 없을 경우 처리
//					throw new RuntimeException("JWT 유틸리티를 사용할 수 없습니다.");
//				}
//			} else {
//				// 비밀번호가 일치하지 않을 경우
//				throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//			}
//		} else {
//			// 해당 이메일의 의사가 존재하지 않을 경우
//			throw new IllegalArgumentException("해당 이메일로 등록된 의사를 찾을 수 없습니다.");
//		}
//	}
public DoctorLoginResponseDTO login(String email, String password) {
	Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);
	if (doctorOptional.isPresent()) {
		Doctor doctor = doctorOptional.get();
		if (passwordEncoder.matches(password, doctor.getPassword())) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("id", doctor.getId());
			claims.put("email", doctor.getEmail());
			// 필요한 다른 정보들을 추가
			
			if (jwtUtil.isPresent()) {
				try {
					String accessToken = jwtUtil.get().generateToken(claims, 30);
					String refreshToken = jwtUtil.get().generateToken(claims, 60 * 24);
					
					return DoctorLoginResponseDTO.builder()
							.id(doctor.getId())
							.hospital(doctor.getHospital())
							.email(doctor.getEmail())
							.password(doctor.getPassword()) // 주의: 보안상 비밀번호를 그대로 반환하는 것은 위험할 수 있습니다.
							.createdAt(doctor.getCreatedAt())
							.doctorStatus(doctor.getDoctorStatus())
							.accessToken(accessToken)
							.refreshToken(refreshToken)
							.build();
				} catch (Exception e) {
					// JWT 생성 중 오류 발생 시 처리
					throw new RuntimeException("토큰 생성 중 오류가 발생했습니다.", e);
				}
			} else {
				// JWTUtil이 없을 경우 처리
				throw new RuntimeException("JWT 유틸리티를 사용할 수 없습니다.");
			}
		} else {
			// 비밀번호가 일치하지 않을 경우
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}
	} else {
		// 해당 이메일의 의사가 존재하지 않을 경우
		throw new IllegalArgumentException("해당 이메일로 등록된 의사를 찾을 수 없습니다.");
	}
}
}