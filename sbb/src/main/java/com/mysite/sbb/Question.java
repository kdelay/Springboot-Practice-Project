package com.mysite.sbb;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
