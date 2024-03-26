package com.mysite.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 특정 엔티티 또는 데이터를 찾을 수 없을 때 발생시키는 예외 클래스
// 예외가 발생할 경우 설정된 HTTP 상태코드와 이유를 포함한 응답을 생성하여 클라이언트에게 반환한다.
// 404 오류 반환 설정됨
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DataNotFoundException(String message) {
		super(message);
	}
}
