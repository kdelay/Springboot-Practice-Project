package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 데이터 저장, 조회, 수정, 삭제를 할 수 있도록 도와주는 인터페이스. 테이블에 접근한다.
// 이와 같이 데이터를 관리하려면 데이터베이스와 연동하는 JPA Repository가 필요하다.
public interface QuestionRepository extends JpaRepository<Question, Integer>{ // 기본 값이 Integer이므로 추가로 저장한다.
	// 제목이 동일한 건 조회하기
	Question findBySubject(String subject);
	// 제목과 내용이 동일한 건 조회하기
	Question findBySubjectAndContent(String subject, String content);
	// subject 열 값들 중에 특정 문자열을 포함하는 데이터 조회하기
	List<Question> findBySubjectLike(String subject);
	// 페이징 기능 추가
	Page<Question> findAll(Pageable pageable);
	// Specification을 통해 질문 검색하기 위한 메서드
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	// Specification 대신 쿼리를 직접 작성하여 검색 기능을 구현할 수 있다.
	@Query("select "
			+ "distinct q "
			+ "from Question q "
			+ "left outer join SiteUser u1 on q.author=u1 "
			+ "left outer join Answer a on a.question=q "
			+ "left outer join SiteUser u2 on a.author=u2 "
			+ "where "
			+ "	q.subject like %:kw% "
			+ "	or q.content like %:kw% "
			+ "	or u1.username like %:kw% "
			+ "	or a.content like %:kw% "
			+ "	or u2.username like %:kw% ")
	Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
