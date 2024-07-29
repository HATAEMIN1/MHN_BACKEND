package com.project.mhnbackend.freeBoard.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.mhnbackend.common.exception.CMRespDTO;
import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.common.util.JWTUtil;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.EditFreeBoardDTO;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardDTO;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseDTO;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseWithCommentsDTO;
import com.project.mhnbackend.freeBoard.service.FreeBoardService;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FreeBoardController {

	@Autowired
	private FreeBoardService freeBoardService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private FileUploadUtil fileUploadUtil;

//    @PostMapping("/boards")
//    public Map<String, Long> registerFreeBoard(FreeBoardRequestDTO freeBoardRequestDTO){
//    	List<MultipartFile> files = freeBoardRequestDTO.getFiles();
//    	List<String> uploadFileNames = fileUploadUtil.saveFiles(files);
//    	
//    	freeBoardRequestDTO.setUploadFileNames(uploadFileNames);
//    	
//    	Long id = freeBoardService.register(freeBoardRequestDTO);
//    	return Map.of("Result", id);
//    }

//    @GetMapping("/boards")
//    public ResponseEntity<?> freeBoardList(@PageableDefault(size = 4) Pageable pageable) {
//        Page<Object[]> freeBoards = freeBoardService.pageFreeBoard(pageable);
//        return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoards), HttpStatus.OK);
//    }
//    
//    @GetMapping("/boards/view")
//    public FreeBoard getFreeBoard(@RequestParam("freeBoardId") Long freeBoardId) {
//    	return freeBoardService.getFreeBoard(freeBoardId);
//    }
	@GetMapping("/boards")
	public ResponseEntity<?> freeBoardList(@PageableDefault(size = 4) Pageable pageable) {
		Page<FreeBoardResponseDTO> freeBoards = freeBoardService.pageFreeBoard(pageable);
		return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoards), HttpStatus.OK);
	}

//	@GetMapping("/boards/view")
//	public ResponseEntity<?> getFreeBoard(@RequestParam("freeBoardId") Long freeBoardId, @RequestParam("memberId") Long memberId) {
//	    FreeBoardResponseWithCommentsDTO freeBoard = freeBoardService.getFreeBoard(freeBoardId, memberId);
//	    return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoard), HttpStatus.OK);
//	}
	@GetMapping("/boards/view")
	public ResponseEntity<?> getFreeBoard(@RequestParam("freeBoardId") Long freeBoardId,
			@RequestParam("memberId") Long memberId) {
		FreeBoardResponseWithCommentsDTO freeBoard = freeBoardService.getFreeBoard(freeBoardId, memberId);
		return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoard), HttpStatus.OK);
	}

	@PutMapping("/boards/view")
	public void editFreeBoard(@RequestParam("freeBoardId") Long freeBoardId,
			@RequestBody EditFreeBoardDTO editFreeBoardDTO) {
		freeBoardService.editFreeBoard(freeBoardId, editFreeBoardDTO);
	}

//    @DeleteMapping("/boards/view")
//    public void deleteFreeBoard(@RequestParam("freeBoardId") Long freeBoardId) {
//        freeBoardService.deleteFreeBoard(freeBoardId);
//    }
//    @DeleteMapping("/view")
//    public ResponseEntity<?> deleteFreeBoard(@RequestParam("freeBoardId") Long freeBoardId, @RequestParam("memberId") Long memberId) {
//        freeBoardService.deleteFreeBoard(freeBoardId, memberId);
//        return ResponseEntity.ok().build();
//    }
	@DeleteMapping("/boards/view")
	public ResponseEntity<?> deleteFreeBoard(@RequestParam("freeBoardId") Long freeBoardId,
			@RequestParam("memberId") Long memberId) {
		freeBoardService.deleteFreeBoard(freeBoardId, memberId);
		return ResponseEntity.ok().build();
	}

//    @PostMapping("/board")
//    public ResponseEntity<FreeBoard> postFreeBoard(@RequestParam("title") String title,
//                                                   @RequestParam("content") String content,
//                                                   @RequestParam("files") List<MultipartFile> files,
//                                                   HttpServletRequest request) {
//        // JWT 토큰에서 사용자 정보 추출
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String token = authorizationHeader.substring(7);
//        Map<String, Object> claims;
//        try {
//            claims = JWTUtil.validateToken(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String name = (String) claims.get("name");
//
//        // 로그로 name 확인
//        System.out.println("Extracted name from JWT: " + name);
//
//        // 작성자(Member) 가져오기
//        Member writer = memberRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("Invalid writer name"));
//
//        // DTO 생성
//        FreeBoardDTO freeBoardDTO = FreeBoardDTO.builder()
//                .name(name) // 사용자 이름을 DTO에 설정
//                .title(title)
//                .content(content)
//                .files(files)
//                .uploadFileNames(files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList()))
//                .build();
//
//        // 서비스 메서드 호출하여 게시물 저장
//        FreeBoard savedFreeBoard = freeBoardService.postFreeBoard(freeBoardDTO, writer);
//
//        return ResponseEntity.ok(savedFreeBoard);
//    }

//	@PostMapping("/boards")
//	public ResponseEntity<FreeBoard> createFreeBoard(@RequestParam("title") String title,
//			@RequestParam("content") String content, @RequestParam("memberId") Long memberId,
//			@RequestParam("files") List<MultipartFile> files) {
//
//		FreeBoardRequestDTO freeBoardRequestDTO = FreeBoardRequestDTO.builder().title(title).content(content)
//				.memberId(memberId).files(files)
//				.uploadFileNames(files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList()))
//				.build();
//
//		FreeBoard freeBoard = freeBoardService.createFreeBoard(freeBoardRequestDTO);
//		return ResponseEntity.status(HttpStatus.CREATED).body(freeBoard);
//	}
	@PostMapping("/boards")
    public ResponseEntity<FreeBoard> createFreeBoard(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("memberId") Long memberId) {

        FreeBoardRequestDTO freeBoardRequestDTO = FreeBoardRequestDTO.builder()
                .title(title)
                .content(content)
                .memberId(memberId)
                .files(files)
                .uploadFileNames(files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList()))
                .build();

        FreeBoard freeBoard = freeBoardService.createFreeBoard(freeBoardRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(freeBoard);
    }
	

//    @PostMapping("/boards/like")
//    public ResponseEntity<String> likeFreeBoard(@RequestParam("freeBoardId") Long freeBoardId, Authentication authentication) {
//        Member currentUser = getCurrentUser(authentication);
//        freeBoardService.likeFreeBoard(freeBoardId, currentUser.getId());
//        return ResponseEntity.ok("Liked successfully");
//    }
//
//    @PostMapping("/boards/unlike")
//    public ResponseEntity<String> unlikeFreeBoard(@RequestParam("freeBoardId") Long freeBoardId, Authentication authentication) {
//        Member currentUser = getCurrentUser(authentication);
//        freeBoardService.unlikeFreeBoard(freeBoardId, currentUser.getId());
//        return ResponseEntity.ok("Unliked successfully");
//    }

	@PostMapping("/boards/like")
	public ResponseEntity<String> likeFreeBoard(@RequestParam("freeBoardId") Long freeBoardId,
			@RequestParam("memberId") Long memberId) {
		freeBoardService.likeFreeBoard(freeBoardId, memberId);
		return ResponseEntity.ok("Liked successfully");
	}

	@PostMapping("/boards/unlike")
	public ResponseEntity<String> unlikeFreeBoard(@RequestParam("freeBoardId") Long freeBoardId,
			@RequestParam("memberId") Long memberId) {
		freeBoardService.unlikeFreeBoard(freeBoardId, memberId);
		return ResponseEntity.ok("Unliked successfully");
	}

//	@GetMapping("/boards/likes")
//	public ResponseEntity<?> freeBoardListByLikes(@PageableDefault(size = 4) Pageable pageable,
//			Authentication authentication) {
//		Member currentUser = getCurrentUser(authentication);
//		Page<FreeBoardResponseDTO> freeBoards = freeBoardService.pageFreeBoardByLikes(pageable);
//		return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoards), HttpStatus.OK);
//	}
	@GetMapping("/boards/likes")
	public ResponseEntity<?> freeBoardListByLikes(@RequestParam("memberId") Long memberId,
			@PageableDefault(size = 4) Pageable pageable) {
		Page<FreeBoardResponseDTO> freeBoards = freeBoardService.pageFreeBoardByLikes(pageable, memberId);
		return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoards), HttpStatus.OK);
	}

	private Member getCurrentUser(Authentication authentication) {
		String email = ((UserDetails) authentication.getPrincipal()).getUsername();
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid member email"));
	}

	@GetMapping("boards/search")
	public ResponseEntity<Page<FreeBoardResponseDTO>> searchByTitleLike(@RequestParam("title") String title,
			@PageableDefault(size = 4, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<FreeBoardResponseDTO> freeBoards = freeBoardService.searchByTitleLike(title, pageable);
		return ResponseEntity.ok(freeBoards);
	}

	 @GetMapping("boards/oldest")
	    public ResponseEntity<?> freeBoardListByOldest(@RequestParam("memberId") Long memberId, @PageableDefault(size = 4) Pageable pageable) {
	        Page<FreeBoardResponseDTO> freeBoards = freeBoardService.pageFreeBoardByOldest(pageable, memberId);
	        return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoards), HttpStatus.OK);
	    }
}