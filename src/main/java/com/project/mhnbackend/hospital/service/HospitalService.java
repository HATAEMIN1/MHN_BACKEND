package com.project.mhnbackend.hospital.service;

import com.project.mhnbackend.hospital.domain.Hospital;
import com.project.mhnbackend.hospital.domain.HospitalBMK;
import com.project.mhnbackend.hospital.domain.HospitalComment;
import com.project.mhnbackend.hospital.dto.request.HospitalBMKRequestDTO;
import com.project.mhnbackend.hospital.dto.request.HospitalCommentRequestDTO;
import com.project.mhnbackend.hospital.dto.response.*;
import com.project.mhnbackend.hospital.repository.HospitalBMKRepository;
import com.project.mhnbackend.hospital.repository.HospitalCommentRepository;
import com.project.mhnbackend.hospital.repository.HospitalRepository;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalService {
	//	private static final double SEARCH_DISTANCE = 0.027;  // 약 3km에 해당하는 위도/경도 차이
	private static final double EARTH_RADIUS = 6371; // 지구의 반경 (km)
	private static final double SEARCH_DISTANCE_KM = 1.5; // 검색 반경 (km)
	private static final double SEARCH_DISTANCE = Math.toDegrees (SEARCH_DISTANCE_KM / EARTH_RADIUS);
	private final HospitalRepository hospitalRepository;
	private final HospitalCommentRepository hospitalCommentRepository;
	private final HospitalBMKRepository hospitalBMKRepository;
	private final MemberRepository memberRepository;
	
	//=== 모든 병원 리스트 불러오는거 직접 코딩한거 ===
//	public List<HospitalResponseDTO> getAllHospitals () {
//		List<Hospital> hospitals = hospitalRepository.findAll ();
//		return hospitals.stream ().map ((item) -> HospitalResponseDTO.builder ()
//						.id (item.getId ())
//						.name (item.getName ())
//						.latitude (item.getLatitude ())
//						.longitude (item.getLongitude ())
//						.address (item.getAddress ())
//						.phone (item.getPhone ())
//						.build ())
//				.collect (Collectors.toList ());
//	}
	
	//=== gpt버전 3km이내 반경의 병원 리스트들 겟 ===
	public List<HospitalResponseDTO> getHospitalsInArea (double lat, double lon) {
		double minLat = lat - SEARCH_DISTANCE;
		double maxLat = lat + SEARCH_DISTANCE;
		double minLon = lon - SEARCH_DISTANCE;
		double maxLon = lon + SEARCH_DISTANCE;
		
		List<Hospital> hospitals = hospitalRepository.findHospitalsInArea (minLat, maxLat, minLon, maxLon);
//		//정렬안한 기존버전
//		return hospitals.stream ()
//				.map (this::convertToDTO)
//				.collect (Collectors.toList ());
		//정렬때문에 수정1
		return hospitals.stream ()
				.map (this::convertToDTO)
				.sorted ((h1, h2) -> Double.compare (
						calculateDistance (lat, lon, h1.getLatitude (), h1.getLongitude ()),
						calculateDistance (lat, lon, h2.getLatitude (), h2.getLongitude ())
				))
				.collect (Collectors.toList ());
	}
	
	//정렬때문에 수정2
	private double calculateDistance (double lat1, double lon1, double lat2, double lon2) {
		// 간단한 유클리드 거리 계산 (정확하지는 않지만 정렬 목적으로는 충분)
		double dx = lat1 - lat2;
		double dy = lon1 - lon2;
		return Math.sqrt (dx * dx + dy * dy);
	}

//	private HospitalResponseDTO convertToDTO (Hospital hospital) {
//		return HospitalResponseDTO.builder ()
//				.id (hospital.getId ())
//				.name (hospital.getName ())
//				.latitude (hospital.getLatitude ())
//				.longitude (hospital.getLongitude ())
//				.address (hospital.getAddress ())
//				.phone (hospital.getPhone ())
//				.build ();
//	}
	
	// 병원 상세정보 get
	public HospitalResponseDTO getHospitalView (Long hospitalId) {
		Hospital hospital = hospitalRepository.findById (hospitalId)
				.orElseThrow (() -> new NoSuchElementException ("Hospital not found with id: " + hospitalId));
		return convertToDTO (hospital);
	}
	
	private HospitalResponseDTO convertToDTO (Hospital hospital) {
		return HospitalResponseDTO.builder ()
				.id (hospital.getId ())
				.name (hospital.getName ())
				.latitude (hospital.getLatitude ())
				.longitude (hospital.getLongitude ())
				.address (hospital.getAddress ())
				.phone (hospital.getPhone ())
				.build ();
	}
	
	
	// 병원 코멘트 작성 (post)
	//	public void createHospitalComment (Long userId, Long hospitalId...로 각각쓰는대신 HospitalCommentRequestDTO hospitalCommentRequestDTO로 퉁침) {
	public void createHospitalComment (HospitalCommentRequestDTO hospitalCommentRequestDTO) {
		
		Hospital hospital = hospitalRepository.findById (hospitalCommentRequestDTO.getHospitalId ()).orElseThrow ();
		Member member = memberRepository.findById (hospitalCommentRequestDTO.getMemberId ()).orElseThrow ();
		
		HospitalComment hospitalComment = HospitalComment.builder ()
				.hospital (hospital)
				.member (member)
				.content (hospitalCommentRequestDTO.getComment ())
				.createdAt (LocalDateTime.now ())
				.rating (hospitalCommentRequestDTO.getRating ())
				.build ();
		
		hospitalCommentRepository.save (hospitalComment);
	}
	
	
	// 병원 진료후기 댓글들 get
	public List<HospitalCommentResponseDTO> getHospitalAllComment (Long hospitalId) {
//		List<HospitalComment> hospitalComments = hospitalCommentRepository.findAll();
//		List<HospitalComment> hospitalComments = hospitalCommentRepository.findByHospitalId (hospitalId);
		List<HospitalComment> hospitalComments = hospitalCommentRepository.findByHospitalId (hospitalId, Sort.by (Sort.Direction.DESC, "createdAt"));
		
		return hospitalComments.stream ().map ((comment) -> HospitalCommentResponseDTO.builder ()
						.hospitalId (comment.getHospital ().getId ())
						.comment (comment.getContent ())
						.id (comment.getId ())
						.createdAt (comment.getCreatedAt ())
						.rating (comment.getRating ())
						.build ())
				.collect (Collectors.toList ());
		
		//.DTO꺼 이름 (item.get엔티티꺼이름())
	}
//
//	public HospitalBMK createHospitalBMK (HospitalBMKRequestDTO hospitalBMKRequestDTO) {
//		Hospital hospital = hospitalRepository.findById(HospitalBMKRequestDTO.getHospitalId()).orElseThrow();
//		HospitalBMK hospitalBMK = HospitalBMK.builder ()
//				.hospital(hospital)
//				.build ();
//		return hospitalBMK;
//	}
	
	public String deleteHospitalComment (Long id) {
		hospitalCommentRepository.deleteById (id);
		
		return "삭제가 완료되었습니다";
	}
	
	// 북마크 등록 post
	public HospitalBMK createHospitalBMK (HospitalBMKRequestDTO hospitalBMKRequestDTO) {
		Hospital hospital = hospitalRepository.findById (hospitalBMKRequestDTO.getHospitalId ()).orElseThrow ();
		Member member = memberRepository.findById (hospitalBMKRequestDTO.getMemberId ()).orElseThrow ();
		HospitalBMK hospitalBMK = HospitalBMK.builder ()
				.member (member)
				.hospital (hospital)
				.createdAt (LocalDateTime.now ())
				.build ();
		return hospitalBMKRepository.save (hospitalBMK);  // 생성된 HospitalBMK 객체를 반환
	}
	
	
	// 병원 북마크 상태값 가져오려고 넣은 겟 _ 상세뷰 진입시 이미 체크되어있을지 아닐지 확인해야해서 넣은 api
	public HospitalBMKResponseDTO getHospitalBMK (Long hospitalId, Long memberId) {
		HospitalBMK hospitalBMK = hospitalBMKRepository.findByHospitalBMK (hospitalId, memberId);
//		//totalBMKCount추가
//		int totalBMKCount = hospitalBMKRepository.countTotalBMKByHospital(hospitalId);
		
		
		if (hospitalBMK == null) {
			return null;
		}
		
		return HospitalBMKResponseDTO.builder ()
				.id (hospitalBMK.getId ())
				.hospitalId (hospitalBMK.getHospital ().getId ())
				.memberId (hospitalBMK.getMember ().getId ())
//				// totalBMKCount 추가
//				.totalBMKCount(totalBMKCount)
				.build ();
	}
	
	// BMK 해제 (delete)
	//	public HospitalBMKResponseDTO deleteHospitalBMK (Long id) {
	public String deleteHospitalBMK (Long id) {
		hospitalBMKRepository.deleteById (id);
		
		return "삭제가 완료되었습니다";
	}
	
	
	// 북마크 총 개수
	public HospitalBMKCountResponseDTO getHospitalBMKCount (Long hospitalId) {
		int totalBMKByHospital = hospitalBMKRepository.countTotalBMKByHospital (hospitalId);
		return HospitalBMKCountResponseDTO.builder ()
				.hospitalId (hospitalId)
				.totalBMKCount (totalBMKByHospital)
				.build ();
	}
	
	// 병원 별점 평균
	public HospitalRatingAVGResponseDTO getHospitalRatingAVG(Long hospitalId) {
		Double ratingAVGByHospitalId = hospitalCommentRepository.getAVGRating(hospitalId);
		
		// null 체크 추가
		double avgRating = (ratingAVGByHospitalId != null) ? ratingAVGByHospitalId : 0.0;
		
		return HospitalRatingAVGResponseDTO.builder()
				.hospitalId(hospitalId)
				.ratingAVG(avgRating)
				.build();
	}
//	public HospitalRatingAVGResponseDTO getHospitalRatingAVG (Long hospitalId) {
////		double ratingAVGByHospitalId = hospitalCommentRepository.getAVGRating (hospitalId);
//		Double ratingAVGByHospitalId = hospitalCommentRepository.getAVGRating(hospitalId);
//
//		// null 체크를 추가
//		double avgRating = (ratingAVGByHospitalId != null) ? ratingAVGByHospitalId : 0.0;
//
//		return HospitalRatingAVGResponseDTO.builder ()
//				.hospitalId (hospitalId)
//				.ratingAVG (ratingAVGByHospitalId)
//				.build ();
//	}
	

	
	// 즐겨찾기한 병원 리스트 겟
	public List<HospitalAccountBMKListResponseDTO> getHospitalAccountBMKList (Long memberId) {
		List<HospitalBMK> bookmarks = hospitalBMKRepository.findByMemberId (memberId);
		return bookmarks.stream ()
				.map (bmk -> HospitalAccountBMKListResponseDTO.builder ()
						.id (bmk.getId ())
						.memberId (bmk.getMember ().getId ())
						.hospital (bmk.getHospital ())  // Hospital 객체 그대로 사용
						.build ())
				.collect (Collectors.toList ());
	}
}