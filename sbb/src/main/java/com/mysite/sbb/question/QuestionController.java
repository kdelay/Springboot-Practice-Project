package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// 애너테이션의 생성자 방식으로 QuestionRepository 객체 주입
@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {
	// @RequiredArgsConstructor : Lombok이 제공하는 애너테이션
	// final이 붙은 속성을 포함하는 생성자를 자동으로 만들어준다.
	private final QuestionService questionService; 
	private final UserService userService;
	
	@GetMapping("/list")
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) { // Model 객체는 자바 클래스와 템플릿 간의 연결 고리 역할을 한다.
		// Model 객체에 값을 담아 두면 템플릿에서 그 값을 사용할 수 있다.
		// 컨트롤러의 메서드에 매개변수로 지정하기만 하면 스프링 부트가 자동으로 Model 객체를 생성한다.
		Page<Question> paging = this.questionService.getList(page);
		model.addAttribute("paging", paging);
		return "question_list";
	}
	
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	// 오버로딩을 이용해서 질문 등록 기능 추가하기
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }
	
    // principal 객체는 로그인을 해야만 생성되는 객체인데 로그아웃 상태일 경우 null이어서 오류가 발생한다.
    // 애너테이션을 붙이면 로그인한 경우에만 실행된다.
    @PreAuthorize("isAuthenticated()")
    // 매개변수 subject, content -> QuestionForm 객체로 변경
    // QuestionForm subject, content 속성이 자동으로 바인딩된다. (폼의 바인딩 기능)
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) { // principal 객체가 null일경우 500에러 발생
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		return "redirect:/question/list"; // 질문 저장 후 질문 목록으로 이동
	}
}
