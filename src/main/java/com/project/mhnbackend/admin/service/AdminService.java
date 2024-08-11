package com.project.mhnbackend.admin.service;

import com.project.mhnbackend.admin.domain.BoardReport;
import com.project.mhnbackend.admin.dto.request.BoardReportRequestDTO;
import com.project.mhnbackend.admin.dto.response.BoardReportResponseDTO;
import com.project.mhnbackend.admin.repository.BoardReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final BoardReportRepository boardReportRepository;
	
	// 신고게시글 리스트 전부 겟
	public List<BoardReportResponseDTO> getAllBoardReportList () {
		List<BoardReport> boardReports = boardReportRepository.findAll();
		return boardReports.stream()
				.map(boardReport -> {
					return BoardReportResponseDTO.builder()
							.memberId(boardReport.getMember ().getId ())
							.freeBoard(boardReport.getFreeBoard ())
							.build();
				})
				.collect(Collectors.toList());
	}
}
