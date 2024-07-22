package com.project.mhnbackend.pet.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.mhnbackend.common.util.PetFileUploadUtil;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
import com.project.mhnbackend.pet.service.PetService;



@RestController
@RequestMapping("/api/v1/pets")
public class PetController {
	
    @Autowired
    private PetService petService;
    
    @Autowired
    private PetFileUploadUtil fileUploadUtil;

    
    // 모든 펫 가져오는거
    @GetMapping("/allpet")
    public ResponseEntity<List<Pet>> getAllPet(Pet petentity) {
        return petService.allPetGet(petentity);
    }
    
    // 펫 한마리 정보 가져오는거
//    @GetMapping("/{id}")
//    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id) {
//        return petService.getPetById(id);
//    }
    
    
    // 펫 등록하는거
    @PostMapping("/addpet")
    public ResponseEntity<String> savePet(@ModelAttribute PetRequestDTO dto) {
        System.out.println("Received pet data: " + dto); // 디버깅용 로그
        System.out.println("Received image: " + (dto.getPetImage() != null ? dto.getPetImage().getOriginalFilename() : "null")); // 디버깅용 로그
        String result = petService.petSave(dto);
        return ResponseEntity.ok(result);
    }
    
    // 이미지 가져오는 엔드포인트 추가
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> getPetImage(@PathVariable("fileName") String fileName) {
        return fileUploadUtil.getFile(fileName);
    }

    // 펫 삭제하는거
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
        return petService.deletePet(id);
    }
    
    
}