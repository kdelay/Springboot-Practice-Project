package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 데이터를 변경할 때 Setter 메서드 말고 새로운 변경 메서드를 추가로 작성해서 사용
@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Increasement
	private Integer id; // 기본키
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate; // 데이터베이스의 테이블에서는 create_date로 설정됨
	
	// 답변을 참조할 때 question.getAnswerList() 호출
	// mappedBy : 참조 엔티티의 속성명 정의
	// 질문 -> 답변 참조 기능
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) // -> 질문을 삭제할 경우 답변도 함께 삭제 cascade
	private List<Answer> answerList;
	
	// 글쓴이
	@ManyToOne
    private SiteUser author;
	
}
