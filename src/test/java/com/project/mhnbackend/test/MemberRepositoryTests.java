//package com.project.mhnbackend.test;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.project.mhnbackend.member.domain.Member;
//import com.project.mhnbackend.member.domain.MemberType;
//import com.project.mhnbackend.member.repository.MemberRepository;
//
//
//
//@SpringBootTest
//public class MemberRepositoryTests {
//
//	@Autowired
//	private MemberRepository memberRepository;
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	@Autowired
//	private AuthenticationManagerBuilder authenticationManagerBuilder;
//	
//	@Test
//	public void testInsertData() {
//		
//		
//		for (int i = 0; i < 30; i++) {
//			Member member = Member.builder()
//					.email("user" + i +"@aaa.com")
//					.password(passwordEncoder.encode("1111"))
//					.name("김으뜸"+ i)
//					.nickName("user" + i)
//					.tel("010123456"+ i)
//					.build();
//			member.addType(MemberType.USER);
//			if(i>15) {
//				member.addType(MemberType.DOCTOR);
//			}
//			if(i>=29) {
//				member.addType(MemberType.ADMIN);
//			}
//			
//			memberRepository.save(member);
//		}
//		
//		
//	}
//	
//	
////	@Test
////	public void testRead() {
////		String email = "user9@aaa.com";
////		Member member = memberRepository.getWithRole(email);
////		
////		System.out.println(member.getMemberRoleList());
////	}
//}