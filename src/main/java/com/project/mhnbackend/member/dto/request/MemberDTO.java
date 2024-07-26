package com.project.mhnbackend.member.dto.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class MemberDTO extends User{
	private Long id;
	private String email,password,name,nickname,tel;
	private List<String> memberTypeList= new ArrayList<>();
	
	public MemberDTO(Long id,String email, String password,String name, String nickname, String tel, List<String> memberTypeList) {
		super(email, password, memberTypeList.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));
		this.id = id;
		this.email= email;
		this.password= password;
		this.name=name;
		this.nickname=nickname;
		this.tel=tel;
		this.memberTypeList=memberTypeList;
	
	}

	public Map<String, Object> getClaims(){
		Map<String, Object> dataMap = new HashMap<>();

		dataMap.put("id", id);
		dataMap.put("email", email);
		dataMap.put("password", password);
		dataMap.put("name", name);
		dataMap.put("nickname", nickname);
		dataMap.put("tel", tel);
		dataMap.put("memberTypeList", memberTypeList);
		
		return dataMap;
	}
	
	
	
	
}

