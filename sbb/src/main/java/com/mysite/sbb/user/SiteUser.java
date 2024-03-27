package com.mysite.sbb.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser { // 스프링 시큐리티에 이미 User클래스가 있기 때문에 다른 클래스명을 사용하는 것이 좋다.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true) // 유일한 값만 저장할 수 있다. 값 중복 불가
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;

}
