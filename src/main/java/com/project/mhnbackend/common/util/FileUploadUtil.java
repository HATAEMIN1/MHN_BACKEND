package com.project.mhnbackend.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.project.mhnbackend.freeBoard.domain.BoardImage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@Component
@Log4j2
@RequiredArgsConstructor
public class FileUploadUtil {

	@Value("${com.study.spring.upload.path}")
	private String uploadPath;
	
	@PostConstruct
	public void init() {
		File tempFolder = new File(uploadPath);
		
		if(!tempFolder.exists()) {
			tempFolder.mkdir();
		}
		
		uploadPath = tempFolder.getAbsolutePath();
		
		log.info("------------------");
		log.info(uploadPath);
	}
	
public List<String> saveFiles(List<MultipartFile> files) {
		
		
		if(files == null || files.size() == 0) {
			return null;
		}
		
		List<String> uploadNames = new ArrayList<>();
		for(MultipartFile multipartFile : files) {
			String saveName = UUID.randomUUID().toString() + "_" 
							+ multipartFile.getOriginalFilename();
			
			Path savePath = Paths.get(uploadPath,saveName);
			
			try {
				Files.copy(multipartFile.getInputStream(), savePath);
				String contentType = multipartFile.getContentType();
				
				if(contentType != null && contentType.startsWith("image")) {
					Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);
					
					Thumbnails.of(savePath.toFile())
					.size(400, 400)
					.toFile(thumbnailPath.toFile());
					
				}
				
				uploadNames.add(saveName);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return uploadNames;
	}
	
}
