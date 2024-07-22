package com.project.mhnbackend.pet.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.mhnbackend.common.util.PetFileUploadUtil;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
import com.project.mhnbackend.pet.repository.PetRepository;




@Service
public class PetService {
	
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private PetFileUploadUtil fileUploadUtil;
  
  // 모든 펫 가져오는거   
    public ResponseEntity<List<Pet>> allPetGet(Pet petentity) {
	List<Pet> allentity =  petRepository.findAll();
	return ResponseEntity.ok(allentity); //바디 
}
    
    // 펫 한마리 정보 가져오는거
    public ResponseEntity<Pet> getPetById(Long id){
    	Optional<Pet> pet = petRepository.findById(id);
    	return pet.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    
    
    // 펫 등록하는거
    public String petSave(PetRequestDTO petdto) {
        String imageFileName = null;
        if (petdto.getPetImage() != null && !petdto.getPetImage().isEmpty()) {
            imageFileName = fileUploadUtil.saveFile(petdto.getPetImage());
        }
        
        Pet petEntity = Pet.builder()
                .name(petdto.getName())
                .kind(petdto.getKind())
                .age(petdto.getAge())
                .petImage(imageFileName)
                .build();
        Pet savedPet = petRepository.save(petEntity);
        return savedPet.getId().toString();
    }
    
    
    // 펫 삭제하는거
    public ResponseEntity<Void> deletePet(Long id) {
        Optional<Pet> petOptional = petRepository.findById(id);
        if(petOptional.isPresent()) {
            Pet pet = petOptional.get();
            if (pet.getPetImage() != null) {
                fileUploadUtil.deleteFiles(List.of(pet.getPetImage()));
            }
            petRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
