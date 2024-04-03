package com.hello.forum.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //Spring Interceptor, Servlet Filter, MVC설정 등을 처리할 수 있음
@Configurable
@EnableWebMvc // MVC와 관련된 여러가지 기능들이 활성화된다
			  // MVC와 관련된 설정들은 이 파일에 작성해야한다.
			  // 그 중 하나가 파라미터 유효성 검사.
public class WebConfig implements WebMvcConfigurer{

	@Value("${app.authentication.check-url-pattern:/**}")
	private String authCheckUrlPattern;
	
	@Value("${app.authentication.ignore-url-patterns:/**}")
	private List<String> authCheckIgnoreUrlPatterns;
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/js/**") //js로 시작하는 모든 url
					.addResourceLocations("classpath:/static/js/");
			registry.addResourceHandler("/css/**")
					.addResourceLocations("classpath:/static/css/");
			
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
	
	// Filter등록.
//	@Bean
//	FilterRegistrationBean<Filter> filter() {
//		
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//		filterRegistrationBean.setFilter(new SessionFilter());
//		filterRegistrationBean.setUrlPatterns(List.of("/board/write", "/board/modify/*", "/board/delete/*"));
//		
//		return filterRegistrationBean;		
//	}
	
	
	/**
	 * 인터셉터를 등록한다
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 세션체크를 하지 않을 URL패턴 정의
//		List<String> excludePatterns = new ArrayList<>();
//		excludePatterns.add("/member/regist/**");
//		excludePatterns.add("/member/login");
//		excludePatterns.add("/board/list");
//		excludePatterns.add("/js/**");
//		excludePatterns.add("/css/**");
//		excludePatterns.add("/error/**");
//		
		//인터셉터 등록하기 (registry.~~)
		// addPathPatterns : interceptor가 개입할 url
		// excludePathPatterns : interceptor가 개입하지 말아야 할 url
		//					(login 여부와 관계없이 모두가 쓸 수 있어야 하므로)
		//request 한번에(=매 요청마다) 아래의 2번의 interceptor가 동작된다
		registry.addInterceptor(new CheckSessionInterceptor())
				.addPathPatterns(this.authCheckUrlPattern)
				.excludePathPatterns(this.authCheckIgnoreUrlPatterns);
		registry.addInterceptor(new BlockDuplicateLoginInterceptor())
				.addPathPatterns("/ajax/member/login", "/ajax/member/regist", 
								"/member/regist", "ajax/member/regist"); //로그인 이후에 이 페이지로 접근하지 못하도록 하는 기능
		
	}
}
