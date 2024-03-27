package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// @PreAuthorize가 동작할 수 있도록 수정
// prePostEnabled가 있어야 @PreAuthorize를 사용할 수 있다.
@EnableMethodSecurity(prePostEnabled = true)
// @Configuration : 스프링의 환경 설정 파일 의미 (스프링 시큐리티를 설정하기 위해 사용)
// @EnableWebSecurity : 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만듬 -> 스프링 시큐리티 활성화
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	// SecurityFilterChain 클래스가 동작하여 모든 요청 URL에 이 클래스가 필터로 적용되어 URL별로 특별한 설정을 할 수 있다.
	// 스프링 시큐리티의 세부 설정은 @Bean을 통해 SecurityFilterChain 빈을 생성하여 설정할 수 있다.
	// 빈(bean) : 스프링에 의해 생성 또는 관리되는 객체 (Controller, Service, Repository...)
	// -> @Bean을 설정하면 자바 코드 내에서 별도로 빈을 정의하고 등록할 수 있다.
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 인증되지 않은 모든 페이지의 요청을 허락한다.
		// 로그인하지 않더라도 모든 페이지에 접근할 수 있다.
		http
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
			// 스프링 시큐리티 CSRF 방어 기능에 의해 H2 콘솔 접근이 거부된다..
			// -> CSRF : 웹 보안 공격, 조작된 정보로 웹 사이트가 실행되도록 속이는 공격 기술
			// 스프링 시큐리티는 이러한 공격을 방지하기 위해 CSRF 토큰을 세션을 통해 발생하고, 폼 전송 시에 해당 토큰을 함께 전송하여 실제 웹 페이지에서 작성한 데이터가 전달되는지 검증한다.
			.csrf((csrf) -> csrf
					.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))) // 해당 URL로 시작하는 모든 URL은 CSRF 검증을 하지 않는다.
			// H2 콘솔의 화면은 프레임 구조로 작성되어있다.
			// 스프링 시큐리티는 웹 사이트의 콘텐츠가 다른 사이트에 포함되지 않도록 하기 위해 X-Frame-Options 헤더의 기본값을 DENY로 설정한다. -> 오류 발생!
			// -> X-Frame-Options 헤더는 클릭재킹 공격을 막기 위해 사용한다. (사용자의 의도와 다른 작업이 수행되도록 속이는 보안 공격 기술)
			.headers((headers) -> headers
					.addHeaderWriter(new XFrameOptionsHeaderWriter(
							XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
			// 스프링 시큐리티에 로그인을 하기 위한 URL 설정
			// 로그인 실패 시 페이지 매개변수로 error가 함께 전달된다. 템플릿에서 ${param.error}로 error 매개변수가 전달되었는지 확인할 수 있다.
			// 스프링 시큐리티를 통해 로그인을 수행하는 방법은 여러 가지가 있다.
			// -> 시큐리티 설정 파일에 사용자 ID와 비밀번호를 직접 등록하여 인증을 처리하는 메모리 방식이 있음
			// -> [사용] DB에서 회원 정보를 조회하여 로그인
			.formLogin((formLogin) -> formLogin
					.loginPage("/user/login") // 로그인을 담당하는 페이지
					.defaultSuccessUrl("/")) // 성공 시 루트 URL로 이동
			// 로그아웃 설정
			.logout((logout) -> logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true))
			;			
		;
		return http.build();
	}
	

	// BCrypt는 직접 new로 생성하는 방식보다는 PasswordEncoder 객체를 빈으로 등록해서 사용하는 것이 좋다.
	// 암호화 방식을 변경하면 BCryptPasswordEncoder를 사용한 모든 프로그램을 일일이 찾아다니며 수정해야 하기 때문이다.
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 로그인 기능을 위한 인증 빈 생성
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
