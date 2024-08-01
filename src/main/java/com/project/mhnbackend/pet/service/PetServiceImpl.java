package com.project.mhnbackend.pet.service;

import com.project.mhnbackend.chart.repository.ChartRepository;
import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
import com.project.mhnbackend.pet.dto.response.PetResponseDTO;
import com.project.mhnbackend.pet.repository.PetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PetServiceImpl implements PetService {
	private final PetRepository petRepository;
	private final FileUploadUtil fileUploadUtil;
	private final MemberRepository memberRepository;
	private final ChartRepository chartRepository;
	//파일 업로드 로직
	public List<String> uploadFile(PetRequestDTO petRequestDTO){
		List<MultipartFile> files = petRequestDTO.getFiles ();
		return fileUploadUtil.saveFiles(files);
	}
	
	@Transactional
	@Override
	public Pet createPet (PetRequestDTO petRequestDTO) {
		List<String> uploadFileNames = uploadFile(petRequestDTO);
		Member member = memberRepository.findById(petRequestDTO.getMemberId ()).orElseThrow ();
		
		Pet pet = Pet.builder ()
				.member(member)
				.name(petRequestDTO.getName ())
				.kind (petRequestDTO.getKind ())
				.age (petRequestDTO.getAge ())
				.build ();
		if(uploadFileNames == null){
			petRepository.save(pet);
			return pet;
		}
		for (String fileName : uploadFileNames) {
			pet.addImageString(fileName);
		}
		petRepository.save(pet);
		return pet;
	}
	
	public List<PetResponseDTO> getAllPet(Long memberId) {
         List<Pet> pets = petRepository.findByMemberId(memberId);
         return pets.stream().map((pet)->
                 PetResponseDTO.builder()
                         .id(pet.getId())
                         .name(pet.getName())
                         .kind(pet.getKind())
                         .age(pet.getAge())
                         .petImage(pet.getPetImage())
                         .build()
                 ).toList();
    }

	@Transactional
	public String deletePet (Long id) {
		chartRepository.deleteByPetId(id);
		petRepository.deleteById (id);
		return "삭제가 완료되었습니다";
	}
}
