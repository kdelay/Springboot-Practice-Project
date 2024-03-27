package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate; 

    // answer.getQuestion().getSubject()를 사용해 질문의 제목에 접근할 수 있다.
    // 질문 엔티티와 연결된 속성이라는 것을 답변 엔티티에 표시해야 한다. (N:1 관계)
    // 부모 Question, 자식 Answer
    @ManyToOne
    private Question question; // 질문 엔티티 참조
    
    // 글쓴이
    @ManyToOne
    private SiteUser author;
}
