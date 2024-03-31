package com.hello.forum.beans;

import org.springframework.web.servlet.HandlerInterceptor;

import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.StringUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckSessionInterceptor implements HandlerInterceptor{

	
	//preHandle : controller가 실행되기 전에 가로채는것
	/** Controller실행 전 Interceptor가 개입
	 * @param request 브라우저가 서버에게 요청한 정보
	 * @param response 서버가 브라우저에게 응답할 정도
	 * @param handler 실행할 controller
	 * @return controller 실행을 계속할 것인지 여부 (false 일 경우, 컨트롤러 실행을 하지 않고 즉시 응답해버린다.)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, 
								HttpServletResponse response, Object handler)
			throws Exception {
		//요청객체에서 session을 얻어온다.
		//원래 HttpSession은 request.getSession()으로 얻어와야한다.
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_");
		
		// 세션이 없는경우 (->로그인페이지로 보내야함)
		if(memberVO == null) {
			
			//원래 가려했던 url 정보 -> 현재 요청중인 url을 확인.
			  //GET, POST, PUT, DELETE, FETCH 등등..
			String httpMethod = request.getMethod().toLowerCase();
			String uri = request.getRequestURI();
//			String url = request.getRequestURL().toString();
			String queryString = request.getQueryString();
			
			//콘솔에는 로그인 안했을때만 아래정보 찍히게 될 것
			System.out.println("HttpMethod: " + httpMethod );
			System.out.println("uri: " + uri );
//			System.out.println("url: " + url );
			System.out.println("queryString: " + queryString );

			if (httpMethod.equals("get")) {
				
				String nextUrl = uri;
				if (!StringUtils.isEmpty(queryString)) {
					nextUrl += "?" + queryString;
				}
				
				//Model로 로그인 이후에 요청할 주소를 보내준다
				request.setAttribute("nextUrl", nextUrl);
			}
			
			//get http method일때만 next값을 보내준다
			//post http method일때는 무시한다
//			if(method.equals("get")){
//				
//			}
			
			//로그인화면을 보여주기위한 RequestDispatcher객체를 만든다
			RequestDispatcher rd = 
					request.getRequestDispatcher("/WEB-INF/views/member/memberlogin.jsp");
			//로그인 화면으로 이동시킨다(URL은 바뀌지 않는다)
			// rd.forward :ex)(write페이지에 로그인없이 접속시 )현재 url바뀌지 않고도 로그인 화면이 보인다(로그인수행)
			rd.forward(request, response);
			//컨트롤러 실행을 하지 않고 즉시응답 시켜버림
			return false;
		}
		
		
		return true;
	}
	
	
	/** Controller가 실행 된 후에 Interceptor가 개입
	 * @param request 브라우저가 서버에게 요청한 정보
     * @param response 서버가 브라우저에게 응답할 정보
     * @param handler 실행한 Controller
     * @param modelAndView Controller가 반환한 ModelAndView 객체.
     *		Controller가 String을 반환해도 ModelAndView로 변환된다.
	 * 
	 */
//	@Override
//	public void postHandle(HttpServletRequest request, 
//							HttpServletResponse response, Object handler,
//							ModelAndView modelAndView) throws Exception {
//		HandlerInterceptor.super.postHandle(request, response, 
//											handler, modelAndView);
//	}
	
	
	/**
	 * 요청과 응답처리가 모두 완료된 후 intercepter가 개입
	 * @param request 브라우저가 서버에게 요청한 정보
	 * @param response 서버가 브라우저에게 응답할 정보
	 * @param handler 실행한 Controller
	 * @param ex 실행 중 발생한 예외 객체
	 *			 예외가 발생하지 않았다면 null이다.
	 */
//	@Override
//	public void afterCompletion(HttpServletRequest request, 
//								HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
	
}
