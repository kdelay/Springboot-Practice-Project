package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	// new 객체가 아닌 Bean으로 등록해서 사용
	private final PasswordEncoder passwordEncoder;
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		// 비밀번호 암호화 저장 비크립트(BCrypt) 해시 함수 사용
		// BCrypt : 해시 함수의 하나로 주로 비밀번호와 같은 보안 정보를 안전하게 저장하고 검증할 때 사용하는 암호화 기술
		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); -> 사용 X. Bean으로 등록할 것
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		return user;
	}
	
	// princial 객체를 사용하면 로그인한 사용자명을 알 수 있으므로 사용자명으로 SiteUser 객체를 조회할 수 있다.
	public SiteUser getUser(String username) {
		Optional<SiteUser>  siteUser = this.userRepository.findByusername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	}
}
