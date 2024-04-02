package com.hello.forum.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ControllerAdvice
 * Base package (com.hello.forum) 아래에서 발생하는 
 * 처리되지않은 모든 예외들을 controllerAdvice가 처리해준다.
 * 즉, catch가 안된것들만 여기에서 전부 다 처리해 주겠다는 것
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * PageNotFoundException이 발생했을때, 동작하는 메소드
	 * @param pnfe ControllerAdvice까지 처리되지 않은 PageNotFoundException 객체
	 * @return 에러페이지
	 */
	@ExceptionHandler(PageNotFoundException.class)
	public String viewPageNotFoundPage(PageNotFoundException pnfe, Model model) {
		
		model.addAttribute("message", pnfe.getMessage());
		
		// error폴더의 404페이지를 보여줘라!
		return "error/404";
	}
	
	
}
