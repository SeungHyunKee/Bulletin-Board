package com.hello.forum.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroduceController {
	
	@GetMapping("/introduce")
	public String viewIntroducePage(Model model) { //url로 전달
		
		// 데이터 전달
		model.addAttribute("name", "기승현");
		model.addAttribute("age", 27);
		model.addAttribute("city", "서울");
		
		return "introduce"; //해당 페이지를 브라우저로 전달
	}
}



// http://localhost:8080/introduce