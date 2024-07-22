package com.project.mhnbackend.freeBoard.dto.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EditFreeBoardDTO {
	private String title;
	private String content;
	
//	private List<MultipartFile> files = new ArrayList<>();
//	
//	private List<String> uploadFileNames = new ArrayList<>();
}
