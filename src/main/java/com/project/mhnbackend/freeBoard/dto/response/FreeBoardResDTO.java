package com.project.mhnbackend.freeBoard.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FreeBoardResDTO {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime createDate;
	private List<MultipartFile> files; 

}
