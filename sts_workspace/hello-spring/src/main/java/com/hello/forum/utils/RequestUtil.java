package com.hello.forum.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public abstract class RequestUtil {

	private RequestUtil() {
		
	}
	
	public static HttpServletRequest getRequest() {
		
		//실무에서는 아래 한줄로 사용
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//				.getRequestAttributes()).getRequest();
		
		//Spring이 관리하는 Request 객체를 얻어온다.
		//요청과 관련된 모든 정보를 spring이 돌려줌
		RequestAttributes ra = RequestContextHolder.getRequestAttributes(); 
	
		//Request객체를 가져오기 위해서 ServletRequestAttributes 로 변경한다
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
	
		//요청정보를 꺼내서 반환시킴
		return sra.getRequest();
	}
	
}
