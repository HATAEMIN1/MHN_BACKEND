package com.project.mhnbackend.security.service;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.dto.request.MemberDTO;
import com.project.mhnbackend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService{
	private final MemberRepository memberRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("========================#2 loadUserByUserName==============================" + username);
		log.info("username은"+ username);
		Member member = memberRepository.getWithRole(username);
		if(member == null) {
			throw new UsernameNotFoundException("not found");
		}
		MemberDTO memberDTO = new MemberDTO(
				member.getId(),
				member.getEmail(),
				member.getPassword(),
				member.getName(),
				member.getNickName(),
				member.getTel(),
				member.getMemberTypeList().stream().map(
						memberTypeList -> memberTypeList.name()).collect(Collectors.toList()));
		log.info("멤버 dto는");
		log.info(memberDTO);
		return memberDTO;
	}
}
