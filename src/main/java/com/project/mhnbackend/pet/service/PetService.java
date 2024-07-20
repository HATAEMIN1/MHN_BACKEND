package com.project.mhnbackend.pet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
import com.project.mhnbackend.pet.repository.PetRepository;



@Service
public class PetService {
	
    @Autowired
    private PetRepository petRepository;
  
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
        Pet petEntity = Pet.builder()
                .name(petdto.getName())
                .kind(petdto.getKind())
                .age(petdto.getAge())
                .build();
        		petRepository.save(petEntity);
        return null;
    }
    
    // 펫 삭제하는거
    public ResponseEntity<Void> deletePet(Long id) {
    	if(petRepository.existsById(id)) {
    		petRepository.deleteById(id);
    		return ResponseEntity.noContent().build();
    	} else {
    		return ResponseEntity.notFound().build();
    	}
    }
    
}