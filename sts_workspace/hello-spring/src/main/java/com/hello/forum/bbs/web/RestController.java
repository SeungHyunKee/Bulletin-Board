package com.hello.forum.bbs.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * RestController : 모든요청이 ajax로 들어오고 나감
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController { //@Controller와 @ResponseBody 가 하나로 합쳐진 Annotation

	@AliasFor(annotation = Controller.class)
	String value() default "";
	
}
