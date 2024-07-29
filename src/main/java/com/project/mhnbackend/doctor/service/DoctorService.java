package com.project.mhnbackend.doctor.service;

import com.project.mhnbackend.doctor.domain.Doctor;
import com.project.mhnbackend.doctor.dto.request.DoctorSignUpRequestDTO;
import com.project.mhnbackend.doctor.dto.response.DoctorListResponseDTO;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class DoctorService {
	private DoctorRepository doctorRepository;

	private PasswordEncoder passwordEncoder;
	
	private RegisterMailForDoctor registerMailForDoctor;
	private HospitalRepository hospitalRepository;
	
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
	
	
	
	
	private Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<> ();
	private Map<String, SignUpRequestDTO> tempDoctor = new ConcurrentHashMap<>();
	private Map<String, String> verificationCodes = new ConcurrentHashMap<>();
	
	
	public void sendVerificationEmail(String email) throws Exception {
		// 이메일 인증 코드 생성 및 전송
		String ePw = registerMailForDoctor.sendSimpleMessage(email);
		verificationCodes.put(email, ePw);
	}
	
	
	public boolean isEmailExists (String email) {
		return doctorRepository.findByEmail(email).isPresent();
	}
	
	public boolean isEmailVerified(String email) {
		return verifiedEmails.getOrDefault(email, false);
	}
	
	
	public void registerDoctor(DoctorSignUpRequestDTO doctorSignUpRequestDTO) throws Exception {
		String email = doctorSignUpRequestDTO.getEmail ();
		Hospital hospital = hospitalRepository.findById (doctorSignUpRequestDTO.getHospitalId ()).orElseThrow ();
		
		if (!verifiedEmails.getOrDefault(email, false)) {
			throw new IllegalArgumentException("이메일 인증이 필요합니다.");
		}

		if (isEmailExists(email)) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}


		Doctor doctor = Doctor.builder()
				.email(email)
				.password(passwordEncoder.encode(doctorSignUpRequestDTO.getPassword()))
				.hospital (hospital)
				.createdAt (LocalDateTime.now ())
				.doctorStatus (Doctor.DoctorStatus.PENDING)
				.build();
		doctorRepository.save(doctor);

		// Clean up temporary storage
		verifiedEmails.remove(email);
	}
	
	public void confirmEmail (String email, String code) {
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
	public List<DoctorListResponseDTO> getAllDoctorLists () {
		List<Doctor> doctors = doctorRepository.findAll ();
		return doctors.stream()
				.map(doctor -> {
					Hospital hospital = hospitalRepository.findById(doctor.getHospital ().getId ())
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
	
	public DoctorStatusPutResponseDTO patchDoctorStatus (Long id) {
		Optional<Doctor> doctor = doctorRepository.findById (id);
		doctor.get ().setDoctorStatus (Doctor.DoctorStatus.FULFILLED);
		doctorRepository.save(doctor.get());
		return DoctorStatusPutResponseDTO.builder ()
				.id (id)
				.hospital (doctor.get ().getHospital ())
				.email(doctor.get ().getEmail())
				.password(doctor.get ().getPassword())
				.createdAt(doctor.get ().getCreatedAt())
				.doctorStatus (Doctor.DoctorStatus.FULFILLED)
				.build ();
	}
}

