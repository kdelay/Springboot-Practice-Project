package com.mysite.sbb.user;

import lombok.Getter;

@Getter
public enum UserRole { // Enum 자료형(열거 자료형)
	// 스프링 시큐리티는 인증뿐만 아니라 권한도 관리한다.
	// 스프링 시큐리티는 사용자 인증 후에 사용자에게 부여할 권한과 관련된 내용이 필요하다.
	// 관리자, 사용자 상수와 각 상수에 값 부여
	ADMIN("ROLE_ADMIN"), 
	USER("ROLE_USER");
	
	UserRole(String value) {
		this.value = value;
	}
	private String value;
}
