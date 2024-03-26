package com.mysite.sbb.question;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/* 서비스가 필요한 이유 
 * - 복잡한 코드를 모듈화할 수 있다.
 *	중복된 코드 최소화 가능 
 * - 엔티티 객체를 DTO 객체로 변환할 수 있다.
 * 	엔티티 클래스는 데이터베이스와 직접 맞닿아 있는 클래스이므로 컨트롤러/템플릿 엔진에 전달해 사용하는 것은 좋지 않다.
 * 	엔티티 클래스를 대신해 사용할 DTO(Data Transfer Object) 클래스가 필요하다.
 * 	서비스에서 엔티티 객체를 DTO 객체로 변환하는 작업을 수행한다.
 * 
 * 	Controller <-> Service(Entity<->DTO객체 변환 후 전달) <-> Repository
 */

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository questionRepository;
	
	public List<Question> getList() {
		return this.questionRepository.findAll();
	}

}
