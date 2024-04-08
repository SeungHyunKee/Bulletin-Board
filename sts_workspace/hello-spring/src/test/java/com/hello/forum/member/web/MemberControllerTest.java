package com.hello.forum.member.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.hello.forum.member.service.MemberService;

@WebMvcTest(MemberController.class) //이 컨트롤러를 테스트하겠다!
@Import(MemberService.class) //@Import에는 @Autowired에 들어가는것만 써주면 됨
public class MemberControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MemberService memberService;
	
	@Test
	@DisplayName("회원가입페이지 보여주기 테스트")
	public void viewRegistMemberPageTest() throws Exception {
		//memberservice안쓰므로 given 안씀
		//when -> mvc 통해서 실제 request 전송
		mvc.perform(get("/member/regist"))
			// 응답받은 데이터를 출력한다
			.andDo(print())
			//status().isOk() -> HttpServletResponse의 http status 코드가 200인지 확인
			// (잘못된 url이었다면 404 or 500이 나왔을것)
			.andExpect(status().isOk())
			.andExpect(view().name("member/memberregist")); //view의 이름을 검사함	
	}
	
	
	//responsebody 가 있기때문에 응답내용은 json으로 올것
	// service가 있기때문에 Given 작성해줘야됨
	@Test
	@DisplayName("이메일 중복검사 테스트")
	public void checkAvailableEmailTest() throws Exception {
		
		//Given  -> given클릭하고  ctrl + 1 누르면 given 만 쓸수 있도록 import 해줌
		given(this.memberService.checkAvailableEmail("user01@gmail.com"))
		.willReturn(true); 
		
		//when        --> get방식이니 ?email=aaa@aaa.com 이런식으로 데이터 전송될것
		mvc.perform(get("/ajax/member/regist/available?email=user01@gmail.com")) //이 url을 호출하겠다
			.andDo(print())
			//status 체크
			.andExpect(status().isOk())
			.andExpect(content().string( containsString("user01@gmail.com")))
			.andExpect(content().string( containsString("true")))
			.andExpect(content().string( containsString("\"available\":true")))
			.andExpect(content().string( containsString("\"email\":\"user01@gmail.com\"")));
	
		//verify
		verify(this.memberService).checkAvailableEmail("user01@gmail.com");
	
	}
	
	
	@Test
	@DisplayName("회원가입 파라미터 유효성검사 테스트")
	private void doRegistMemberFailTest() throws Exception {
		// when & then
		mvc.perform(post("/member/regist"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "이메일을 입력해주세요"));		
		
		mvc.perform(post("/member/regist")
			.param("email", "user01gmail.com"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "이메일을 형식을 지켜주세요"));	
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "이름을 입력해주세요"));	
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com")
				.param("name", "testname"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "비밀번호를 입력해주세요"));
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com")
				.param("name", "testname")
				.param("password", "sdf"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "비밀번호를 10글자이상 입력해주세요."));
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com")
				.param("name", "testname")
				.param("password", "sdfsefsefses"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "비밀번호 확인을 입력하세요"));
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com")
				.param("name", "testname")
				.param("password", "sefsefassef")
				.param("confirmpassword", "fsefsesses1"))

			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "비밀번호확인을 비밀번호와같게 입력하세요"));
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com")
				.param("name", "sefsefsfe")
				.param("password", "sdf")
				.param("confirmpassword", "fsefsesses1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "비밀번호 형식과 다릅니다"));
		
		//given
//		given(this.memberService.createNewMember(any())).willReturn(false);
		
		
	}
	
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	private void doRegistMemberTest() throws Exception {
		given(memberService.createNewMember(any()))
		.willReturn(true);
		
		mvc.perform(post("/member/regist")
				.param("email", "user01@gmail.com")
				.param("name", "sefsefsfe")
				.param("password", "sdf")
				.param("confirmpassword", "Fsefsesses1"))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/member/login"));
	}
}


