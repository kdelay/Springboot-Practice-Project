package com.mysite.sbb.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

// 애너테이션의 생성자 방식으로 QuestionRepository 객체 주입
@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {
	// @RequiredArgsConstructor : Lombok이 제공하는 애너테이션
	// final이 붙은 속성을 포함하는 생성자를 자동으로 만들어준다.
	private final QuestionService questionService; 
	
	@GetMapping("/list")
	public String list(Model model) { // Model 객체는 자바 클래스와 템플릿 간의 연결 고리 역할을 한다.
		// Model 객체에 값을 담아 두면 템플릿에서 그 값을 사용할 수 있다.
		// 컨트롤러의 메서드에 매개변수로 지정하기만 하면 스프링 부트가 자동으로 Model 객체를 생성한다.
		List<Question> questionList = this.questionService.getList();
		model.addAttribute("questionList", questionList); // Model 객체에 'questionList'라는 이름으로 저장함
		return "question_list";
	}
	
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}

}
