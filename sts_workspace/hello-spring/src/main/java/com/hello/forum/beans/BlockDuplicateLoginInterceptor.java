package com.hello.forum.beans;

import org.springframework.web.servlet.HandlerInterceptor;

import com.hello.forum.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BlockDuplicateLoginInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//세션이 있을경우에만 block하도록
		
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_");
		
		if(memberVO != null) { //즉, 로그인을 했다면
			response.sendRedirect("/board/search");
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
