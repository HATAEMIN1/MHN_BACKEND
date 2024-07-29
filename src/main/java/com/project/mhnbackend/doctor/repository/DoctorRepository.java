	package com.project.mhnbackend.doctor.repository;
	
	import com.project.mhnbackend.doctor.domain.Doctor;
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	
	import java.util.Optional;
	
	@Repository
	public interface DoctorRepository extends JpaRepository<Doctor, Long> {
		Optional<Doctor> findByEmail (String email);
		
		//=======================회원가입 절차 끝=======================
	}
