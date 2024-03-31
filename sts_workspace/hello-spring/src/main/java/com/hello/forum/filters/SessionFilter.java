package com.hello.forum.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class SessionFilter
 */
public class SessionFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public SessionFilter() {
        super();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// Request를 Servlet(DispatcherServlet)에게 보내기 전에 필터링 코드 실행하는 위치
		
		// 세션을 가져와서 로그인 여부를 판단
		// Session을 가져오려면, HttpServletRequest에서 가져와야 한다.
		// request 파라미터는 ServletRequest
		// HttpServletRequest is a ServletRequest이므로 
		// HttpServletRequest는 ServletRequest를 상속한 클래스 -> 형변환 시켜주면 됨
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		
		//'로그인을 안했다면, 로그인 페이지로 이동' 코드 위치
		if (session.getAttribute("_LOGIN_USER_")==null) {
			//sendRedirect()는 HttpServletResponse에서 사용가능.
			// HttpServletResponse는 ServletResponse를 상속한 클래스
			// -> HttpServletResponse is a ServletResponse
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			//return "redirect:/member/login"; 과 동일한 코드임(아래코드)
			httpResponse.sendRedirect("/member/login");
			return; //void return 해주면 원래 동작해야하는 servlet에서의 response를 안해주게 된다
		}
		
		
		chain.doFilter(request, response);
		
		//servlet(DispatcherService)에서 보내준 Response를 검사하는 필터링 코드 실행
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	
	
	
}
