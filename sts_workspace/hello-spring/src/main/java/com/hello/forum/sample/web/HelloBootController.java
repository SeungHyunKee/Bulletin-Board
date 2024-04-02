package com.hello.forum.sample.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hello.forum.beans.CheckSessionInterceptor;

/*
 * Servlet: HelloBootServlet.java
 *    web.xml // 이 있어야 servlet은 객체를 만들어줌!
 *   <servlet>
 *      <servlet-name></servlet-name>
 *      <servlet-class></servlet-name>
 *       
 */

//@controller가 붙어잇어야만 spring이 인스턴스로 만들어주는 대상이 됨
//@Controller 자체가 servlet의 역할을 하고있다고 생각하기
@Controller
public class HelloBootController {
	
	
	private Logger logger = LoggerFactory.getLogger(HelloBootController.class);

	public HelloBootController() {
		// Spring이 호출한다!, 즉, 생성된객체를 Bean Cotainer에 보관한다
		logger.info("HelloBootControlelr() 호출됨.");
		logger.info(this.toString());
	}
	
	// GetMapping의 역할 = doGet(); => 브라우저와 서버가 통신(데이터를 주고받는)할수있는 end-point == controller
	@GetMapping("/hello")
	// hello -> servlet-mapping > url-pattern 값
	public ResponseEntity<String> hello() {
		ResponseEntity<String> responseBody = 
				new ResponseEntity<>("Hello Boot Controller - Test", HttpStatus.OK);
		return responseBody;
	}
	
	@GetMapping("/hello-html")
	public ResponseEntity<String> helloHTML(){
		StringBuffer html = new StringBuffer();
		html.append("<!DOCTYPE html> ");
		html.append("<html>");
		html.append("	<head>");
		html.append("		<title>Hello Sprnig</title>");
		html.append("	</head>");
		html.append("	<body>	");
		html.append("		<div> Hello, Spring Controller!</div>");
		html.append("	</body>");
		html.append("<html>");

	    ResponseEntity<String> responseBody = 
	    		new ResponseEntity<>(html.toString(), HttpStatus.OK);
		return responseBody;
	}
	
	@GetMapping("/hello-jsp")
	public String helloJSP(Model model) {
		//myname이라는 키로 springbootsample~을 보낸다
		// servlet으로는 : request.setAttribute("myname", "Spring Boot Sample Application");
		// 보내곳은 데이터 아래처럼 여기에다가 추가하기
		model.addAttribute("myname", "Spring Boot Sample Application");
		model.addAttribute("createDate", "2024-04-04");
		model.addAttribute("author", "james");
		model.addAttribute("version", 1.0);

		// Spring (Boot) 1개의 Servlet이 내장되어있다.
		// 내장되어있는 Servlet이 Controller를 호출.
		// 만약, Controller가 반환시킨 데이터가 String 타입이라면
		// Servlet의 코드(RequestDispatcher ~~~; rd.forward(request, response);를 수행한다.
		// 파일의 이름이 반환되었을 경우, application.yml에 정의된 prefix, suffix를 붙인다.
		// 이걸 붙이고 보면, /WEB-INF/views/helloboot.jsp가 됨
		return "helloboot";
	}
	
	
}
