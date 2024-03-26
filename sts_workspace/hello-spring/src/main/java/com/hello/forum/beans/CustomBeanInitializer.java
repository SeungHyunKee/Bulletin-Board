package com.hello.forum.beans;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PostConstruct;

/**
 * Spring이 기본적으로 생성하는 Bean이외의
 * 개발자가 직접 만든 클래스의 Bean을 Bean Container에 적재시키는 역할
 * 
 * @SpringBootConfiguration의 역할 : 
 * Spring Boot Application이 실행될 때 (=서버를 run 시킬때)
 * Spring Boot가 이 annotation이 적용된 클래스를 찾아 실행시키고
 * 필요한 bean들을 Bean Container에 적재한다.
 */
@SpringBootConfiguration
public class CustomBeanInitializer {

	/**
	 * application.yml파일에 작성된 
	 * 사용자 환경설정 정보들을 읽어와서
	 * 멤버변수로 할당해 둔다.
	 */
	@Value("${app.multipart.base-dir:c:/uploadFiles}") //c:/uploadFiles값이 할당됨
	private String baseDir;
	
	@Value("${app.multipart.obfuscation.enable:false}")
	private boolean enableObfuscation;
	
	@Value("${app.multipart.obfuscation.hide-ext.enable:false}")
	private boolean enableObfuscationHideExt;
	
	@Value("${app.multipart.available-file-list.enable:false}")
	private boolean enableAvailableFileList;
	
	@Value("${app.multipart.available-file-list.list}")
	private List<String> availableFileList;
	
	@Value("${app.multipart.available-file-list.handler: tika}")
	private String fileMimeTypeHandler;
	
	//필요한 데이터의 할당 전 이므로 생성자에서는 값들이 들어가지 않는다
	public CustomBeanInitializer() {
		System.out.println("CustomBeanInitializer 실행됨!!!");
		// 아래의 값들을 보려면 생성자가 생성된 이후의 값들로 봐야됨
		// 즉, 생성자가 실행되는 시점에서는 yml의 값이 할당되지 않는다!
		// 생성자가 실행되고 난 이후의 시점에서 yml의 값이 할당된다!
		System.out.println("baseDir: " + baseDir); //null
		System.out.println("enableObfuscation: " + enableObfuscation); //false
		System.out.println("enableObfuscationHideExt: " + enableObfuscationHideExt); //false

	}
	
	/**
	 * 스프링이 클래스를 객체화 시키고
	 * 필요한 값들이나 객체를 모두 할당한 이후에
	 * @PostContruct가 적용된 메소드를 실행시킨다
	 */
	@PostConstruct
	public void postContructor(){
		System.out.println("-------- 생성자가 실행된 이후의 시점 --------"); 
		System.out.println("> baseDir: " + baseDir); //c:/uploadFiles
		System.out.println("> enableObfuscation: " + enableObfuscation); //true
		System.out.println("> enableObfuscationHideExt: " + enableObfuscationHideExt); //true 
		System.out.println("> availableFileList: " +availableFileList);
		System.out.println("> availableFileList: " +availableFileList.size());
	}
	
	/**
	 * @Bean annotation이 적용된 메소드가 실행되면
	 * 이 메소드가 반환하는 객체를 Bean Container에 적재를 한다
	 * 	(적재를하면, controller, repositroy등에서 마음대로 가져다쓸수있게됨!)
	 * 이때, 메소드의 이름이 Bean 객체의 이름이 된다.
	 * @return
	 */
	// @Bean이 적용된 메소드에는 public을 쓰지 않는다
	@Bean
	FileHandler fileHandler() {
		FileHandler fileHandler = new FileHandler();
		fileHandler.setBaseDir(this.baseDir);
		fileHandler.setEnableObfuscation(this.enableObfuscation);
		fileHandler.setEnableObfuscationHideExt(this.enableObfuscationHideExt);
		fileHandler.setAvailableFileList(this.availableFileList);
		fileHandler.setEnableAvailableFileList(this.enableAvailableFileList);
		fileHandler.setHandler(this.fileMimeTypeHandler);
		return fileHandler;
	}
	
	// 회원가입시 passwordㅇ암호화하기
	//SHA알고리즘이 필요한경우 Autowired에서 가져온다!
	@Bean
	SHA sha() {
		
		return new SHA();
	}
}









