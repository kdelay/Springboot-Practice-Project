package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;

import jakarta.transaction.Transactional;


@SpringBootTest
class SbbApplicationTests {
	
	// 의존성 주입 : 스프링이 객체를 대신 생성하여 주입하는 기법
	// 순환 참조 문제가 생길 수 있기 때문에 생성자 Setter를 통한 객체 주입 방식을 권장한다.
	// 테스트 코드의 경우 JUnit이 생성자를 통한 객체 주입을 지원하지 않으므로 테스트 코드 작성 시에만 @Autowired 사용
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void test_dataAddTEst() {
		/* 데이터 추가 테스트 */		
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
	}
	
	@Test
	void test_dataSizeEqual() {
        /* 사이즈 동일한지 확인 테스트 */
		List<Question> all = this.questionRepository.findAll(); // findAll() : SELECT * FROM QUESTION 동일 결과
		assertEquals(2, all.size());
		
		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}
	
	@Test
	void test_subjectEqual() {
		/* 제목이 동일한지 테스트 */
		// findBy + 엔티티의 속성명 으로 레포지토리 메서드를 작성하면 입려간 속성의 값으로 데이터를 조회할 수 있다.
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
	}
	
	@Test
	void test_subjectAndContentEqual() {
		/* 제목과 내용이 동일한지 테스트 */
		// findBy + 엔티티의 속성명 으로 레포지토리 메서드를 작성하면 입려간 속성의 값으로 데이터를 조회할 수 있다.
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}
	
	@Test
	void test_subjectHavingSbbWord() {
		/* 제목에 sbb가 들어가있는 데이터 조회 테스트 */
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}
	
	@Test
	void test_updateSubjectAndSave() {
		/* 제목 수정 및 저장(save) 테스트 */
		// Optional<T> 클래스를 사용하면 NPE(Null Point Exception)을 방지할 수 있다.
		// -> null 이 올 수 있는 값을 감싸는 Wrapper 클래스
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목"); // 제목 수정
		this.questionRepository.save(q);
	}
	
	@Test
	void test_deleteQuestionData() {
		/* 질문 데이터 삭제 테스트 */
		assertEquals(2, this.questionRepository.count()); // 질문 데이터 총 2개
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent()); // 데이터가 존재하는지 여부 확인
		Question q = oq.get();
		this.questionRepository.delete(q); // 삭제
		assertEquals(1, this.questionRepository.count()); // 질문 데이터 총 1개
	}
	
	
	@Test
	void test_saveAnswerData() {
		/* 답변 데이터 저장 테스트 */
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		
		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q); // 어떤 질문의 답변인지 알기 위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	}
	
	@Test
	void test_searchAnswerData() {
		/* 답변 데이터 조회 테스트 */
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}
	
	@Transactional
	@Test
	void test_searchAnswerFromQuestion() {
		/* 질문을 조회한 후 이 질문에 달린 답변 전체를 구하는 테스트 */
		// Question 객체를 조회하고 나면 DB 세션이 끊어짐
		// -> @Transaction 을 사용하면 메서드가 종료될 때까지 DB 세션이 유지된다.
		// -> 테스트 코드에서만 발생하고, 실제 서버에서는 DB 세션이 종료되지 않는다.
		Optional<Question> oq = this.questionRepository.findById(2); 
		assertTrue(oq.isPresent());
		Question q = oq.get();
		
		// 지연(Lazy) 방식 : 데이터를 필요한 시점에 가져오는 방식
		// <-> 즉시(Eager) 방식 : q 객체를 조회할 때 미리 answer 리스트를 모두 가져오는 방식
		List<Answer> answerList = q.getAnswerList();
		
		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}
		
}
