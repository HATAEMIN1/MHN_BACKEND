package com.project.mhnbackend.pet.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
import com.project.mhnbackend.pet.service.PetService;



@RestController
@RequestMapping("/api/v1/pets")
public class PetController {
	
    @Autowired
    private PetService petService;

    // 모든 펫 가져오는거
//    @GetMapping("/allpet")
//    public String getAllPet(Pet petentity) {
//    	petService.allPetGet(petentity);
//    	return null;
//    }
    
    // 모든 펫 가져오는거 gpt
    @GetMapping("/allpet")
    public ResponseEntity<List<Pet>> getAllPet(Pet petentity) {
        return petService.allPetGet(petentity);
    }
    
    // 펫 한마리 정보 가져오는거
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id) {
        return petService.getPetById(id);
    }
    
    
    // 펫 등록하는거
//    @PostMapping("/addpet")
//    public String savePet(PetRequestDTO dto) {    
//    	petService.petSave(dto);
//    	return null;
//    }
    
    // 펫 등록하는거 gpt
    @PostMapping("/addpet")
    public ResponseEntity<Void> savePet(@RequestBody PetRequestDTO dto) {
        petService.petSave(dto);
        return ResponseEntity.ok().build();
    }

    // 펫 삭제하는거
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
        return petService.deletePet(id);
    }


}