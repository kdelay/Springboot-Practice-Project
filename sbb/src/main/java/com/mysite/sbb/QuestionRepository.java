package com.mysite.sbb;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// 데이터 저장, 조회, 수정, 삭제를 할 수 있도록 도와주는 인터페이스. 테이블에 접근한다.
// 이와 같이 데이터를 관리하려면 데이터베이스와 연동하는 JPA Repository가 필요하다.
public interface QuestionRepository extends JpaRepository<Question, Integer>{ // 기본 값이 Integer이므로 추가로 저장한다.
	// 제목이 동일한 건 조회하기
	Question findBySubject(String subject);
	// 제목과 내용이 동일한 건 조회하기
	Question findBySubjectAndContent(String subject, String content);
	// subject 열 값들 중에 특정 문자열을 포함하는 데이터 조회하기
	List<Question> findBySubjectLike(String subject);
}
