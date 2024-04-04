package com.hello.forum.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.hello.forum.utils.AjaxResponse;
import com.hello.forum.utils.RequestUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @ControllerAdvice
 * Base package (com.hello.forum) 아래에서 발생하는 
 * 처리되지않은 모든 예외들을 controllerAdvice가 처리해준다.
 * 즉, catch가 안된것들만 여기에서 전부 다 처리해 주겠다는 것
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory
							.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * PageNotFoundException이 발생했을때, 동작하는 메소드
	 * 
	 * @param pnfe ControllerAdvice까지 처리되지 않은 PageNotFoundException 객체
	 * @return 에러페이지
	 */
	@ExceptionHandler(PageNotFoundException.class)
	public Object viewPageNotFoundPage(PageNotFoundException pnfe, Model model) {

		
		logger.error(pnfe.getMessage(), pnfe);
		
		HttpServletRequest request = RequestUtil.getRequest();
		String uri = request.getRequestURI(); //현재 요청된 주소
		
		//uri가 /ajax/ 로 시작한다면, exception임
		if(uri.startsWith("/ajax/")) {
			AjaxResponse ar = new AjaxResponse();
			ar.append("errorMessage", pnfe.getMessage());
			
			//AJAXResponse를 JSON으로 변환
			Gson gson = new Gson();
			String ajaxJsonResponse = gson.toJson(ar);
			
//			return new ResponseEntity<String>(ajaxJsonResponse, HttpStatus.OK);
//			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
//									.body(ajaxJsonResponse);
			
			
			return ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(ajaxJsonResponse);
		}
		
		model.addAttribute("message", pnfe.getMessage());
		
		// error폴더의 404페이지를 보여줘라!
		return "error/404";
	}
	
	//회원가입과 로그인이 ajax로 처리되고있기떄문에 이렇게 처리함
	@ExceptionHandler({ FileNotFouneException.class, 
		MakeXlsxFileException.class, AlreadyUseException.class,
		UserIdentifyNotMatchException.class, RuntimeException.class}) //예외페이지는 error.500페이지가 보이게 될것
	public Object viewErrorPage(RuntimeException re, Model model) {
		logger.error(re.getMessage(), re);
		HttpServletRequest request = RequestUtil.getRequest();
		String uri = request.getRequestURI(); //현재 요청된 주소
		
		if(uri.startsWith("/ajax/")) {
			
			AjaxResponse ar = new AjaxResponse();
			ar.append("errorMessage", re.getMessage());
			
			Gson gson = new Gson();
			String ajaxJsonResponse = gson.toJson(ar);
			
//			return new ResponseEntity<String>(ajaxJsonResponse, HttpStatus.OK);
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(ajaxJsonResponse);
		}
		
		if (re instanceof AlreadyUseException) {
			AlreadyUseException aue = (AlreadyUseException) re;
			model.addAttribute("email", aue.getEmail());
		}
		
		model.addAttribute("message", re.getMessage());
		return "error/500";
	}
	
}














