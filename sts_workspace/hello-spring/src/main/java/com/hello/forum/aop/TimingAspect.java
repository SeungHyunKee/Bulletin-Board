package com.hello.forum.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect //aop모듈
@Component //Spring Bean 생성해주는 annotation
public class TimingAspect {

	private Logger logger = LoggerFactory.getLogger("timing");
	
	/**
	 * 
	 * pointcut : aop를 적용할 대상을 지정
	 * execution() : 실행단계에서 적용할 대상을 지정
 	 *	public: public 접근제어자를 사용하는 것을 대상으로 함
 	 *	* : 모든 반환타입을 대상으로 함
 	 *	com.forum..service.*ServiceImpl : com.hello.forum아래에 있는 모든패키지 중에서
 	 *									  service패키지 내부에 있는 serviceImpl로 끝나는
 	 *									  모든 클래스를 대상으로 함
 	 *	.*(..) : ServiceImpl로 끝나는 모든 클래스 내부의 모든 메소드를 대상으로 함
 	 *
 	 * => ServiceImpl클래스 밑에 있는 public 모든 반환타입 메소드명()을 대상으로 Aspect를 실행함
 	 *
 	 */
	@Pointcut("execution(public * com.hello.forum..service.*ServiceImpl.*(..))")
	public void aroundTarget() { //aroundtarget : pointcut을 주는 역할만 함
	}
	
	
	/**
	 * 원래 실행될 메소드의 전, 후에 공통코드를 실행한다.
	 * ServiceImpl에 있는 메소드가 실행될 때(Pointcut대상이 실행될 때)
	 * 아래 메소드가 실행된다 == weaving된 코드가 실행된다
	 * @param pjp 원래 실행될 클래스와 메소드의 정보
	 * @return 원래 실행될 메소드의 반환값
	 * @throws Throwable 
	 */
//	@Before
//	@After
//	@AfterReturning
//	@AfterThrowing
	@Around("aroundTarget()")  //Around 하나가 @Before, @After, @AfterReturning, @AfterThrowing다 포함
	public Object timingAdvice(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Object result = null;
		
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			//serviceImpl에서 발생한 예외를 그대로 던진다.
			//controller로 예외가 전달되고, 마지막으로 controllerAdvice로 예외가 전달되어
			// 공통예외를 처리할 수 있기 때문
			throw e;
		} finally {
			stopWatch.stop();
			
			//원래 실행되어야하는 클래스
			String classPath = pjp.getTarget().getClass().getName();
			//원래 실행되어야하는 메소드
			String methodName = pjp.getSignature().getName();
			logger.debug("{}.{} 걸린시간: {}ms", classPath, methodName, stopWatch.lastTaskInfo().getTimeMillis());
		}
		return result;
	}
	
}








