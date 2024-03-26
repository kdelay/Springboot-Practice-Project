package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

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
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if (question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	// 제목과 내용을 입력 받아 이를 질문으로 저장하는 메서드
	public void create(String subject, String content) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q);
	}
	
	// 페이징
	public Page<Question> getList(int page) {
		List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.questionRepository.findAll(pageable);
	}

}
