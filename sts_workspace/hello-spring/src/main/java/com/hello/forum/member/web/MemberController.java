package com.hello.forum.member.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hello.forum.bbs.web.BoardControllerr;
import com.hello.forum.member.service.MemberService;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.AjaxResponse;
import com.hello.forum.utils.StringUtils;
import com.hello.forum.utils.ValidationUtils;
import com.hello.forum.utils.Validator;
import com.hello.forum.utils.Validator.Type;

import jakarta.servlet.http.HttpSession;

//회원가입 요청처리를 위한 컨트롤러
@Controller
public class MemberController {

	private Logger logger = LoggerFactory.getLogger(MemberController.class);

	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/regist")
	public String viewRegistMemberPage() {
		return "member/memberregist";
	}
	
	@ResponseBody //응답하는 데이터를 JSON으로 변환해 클라이언트에게 반환한다.
	// 화면에서 호출하는 방법 : http://localhost:8080/member/regist/available?email=aaa@aaa.com
	@GetMapping("/ajax/member/regist/available")//<- 이 주소에 의해 아래 코드가 실행되고, @RequestParam에는 email이 전달됨
	// 사용자가 입력하는 정보가 들어옴
	public Map<String, Object> checkAvailableEmail(@RequestParam String email){
		
		// 이메일이 있나없나 검증
		// 사용가능한 이메일이라면 true, 아니라면 false를 반환
		boolean isAvailableEmail = this.memberService.checkAvailableEmail(email);
		
		
		
		/**@ResponseBody 를 붙였으므로, 응답되는 데이터들을 스프링이 전부 바꿔서 아래처럼(JSON으로) 보내줌
		 * { "email" : "aaa@aaa.com", "available": false }
		 */
		// 아래 코드가 JSON으로 변환되어 응답시키려면 : @ResponseBody어노테이션을 GetMapping위에 붙여줌
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("email", email);
		responseMap.put("available", isAvailableEmail);
		return responseMap;
	}
	
	// 회원가입 시작
	@PostMapping("/member/regist")
	public String doRegistMember(MemberVO memberVO, Model model) {
		
			boolean isNotEmptyEmail = ValidationUtils.notEmpty(memberVO.getEmail());
			boolean isEmailFormat = ValidationUtils.email(memberVO.getEmail());
			boolean isNotEmptyName = ValidationUtils.notEmpty(memberVO.getName());
			boolean isNotEmptyPassword = ValidationUtils.notEmpty(memberVO.getPassword());
			boolean isEnoughSize = ValidationUtils.size(memberVO.getPassword(), 10);
			boolean isNotEmptyConfirmPassword = ValidationUtils.notEmpty(memberVO.getConfirmPassword());
			
			boolean isEqualsPassword = ValidationUtils.isEquals(memberVO.getPassword(),
										memberVO.getConfirmPassword());
										
			boolean isPasswordFormat = StringUtils.correctPasswordFormat(memberVO.getPassword());
			
			if(! isNotEmptyEmail) {
				//이메일을 입력하지 않았다면
				model.addAttribute("errorMessage", "이메일을 입력해주세요");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			if(! isEmailFormat) {
				// 이메일형식에 맞지 않는다면
				model.addAttribute("errorMessage", "이메일을 형식을 지켜주세요");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			if(! isNotEmptyName) {
				// 이름을 입력하지 않았다면
				model.addAttribute("errorMessage", "이름을 입력해주세요");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			if(!isNotEmptyPassword) {
				model.addAttribute("errorMessage", "비밀번호를 입력해주세요");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			if(!isEnoughSize) {
				model.addAttribute("errorMessage", "비밀번호 최소개수를 맞춰주세요");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";		
			}
			if(!isNotEmptyConfirmPassword) {
				model.addAttribute("errorMessage", "비밀번호 형식에 맞지 않습니다");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			if(!isEqualsPassword) {
				model.addAttribute("errorMessage", "비밀번호가 틀렸습니다");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			if(!isPasswordFormat) {
				model.addAttribute("errorMessage", "비밀번호 형식과 다릅니다.");
				model.addAttribute("memberVO", memberVO);
				return "member/memberregist";
			}
			
			boolean isSuccess = this.memberService.createNewMember(memberVO);
			if (isSuccess) {
				logger.info("회원 가입 성공!");
			}
			else {
				logger.info("회원 가입 실패!");
			}
			return "redirect:/member/login";
		}
	
		@GetMapping("/member/login")
		public String viewLoginPage() {
			return "/member/memberlogin";
		}
		
		@ResponseBody //AJAX반환시키기 위해서 붙이는 annotation
		@PostMapping("/ajax/member/login")
		public AjaxResponse doLogin(MemberVO memberVO, HttpSession session,
									@RequestParam(defaultValue = "/board/list") String nextUrl){ // session:상태를 기억하게하는 객체
			logger.info("nextUrl: " + nextUrl);
			
			//Validation Check (파라미터 유효성 검사) -> 패턴지정해주고, 마지막에 start() 하면 유효셩검사 싹 해주고 검사를 받아올 수 있다
			Validator<MemberVO> validator = new Validator<>(memberVO);
			validator.add("email", Type.NOT_EMPTY, "이메일을 입력해주세요.")
			         .add("email", Type.EMAIL, "이메일 형식이 아닙니다.")
			         .add("password", Type.NOT_EMPTY, "비밀번호를 입력해주세요.")
			         .start();
			
			if(validator.hasErrors()) { //틀린게있으면 아래 처럼 ajax로 돌려줌
				Map<String, List<String>> errors = validator.getErrors();
				return new AjaxResponse().append("errors", errors);
			}
			
			
//			try {
				MemberVO member = this.memberService.getMember(memberVO);
				//로그인이 정상적으로 이루어졌다면 세션을 생성한다
				session.setAttribute("_LOGIN_USER_", member);
				session.setMaxInactiveInterval(20 * 60); //세션이 유지될 수 있는 시간을 지정할수있다(default : 30분)
//			}catch(IllegalArgumentException iae) {
				//로그인이 실패했다면 화면으로 실패사유를 보내준다.
//				return new AjaxResponse().append("errorMessage", iae.getMessage());
//			}
			
			return new AjaxResponse().append("next", nextUrl);
		}
		
		//로그아웃 처리하는 url
		@GetMapping("/member/logout")
		public String doLogout(HttpSession session) {
			//Logout 처리
			//SessionID로 전달된 세션의 모든 정보를 삭제
			session.invalidate();
			return "redirect:/board/list";
		}
		
		
		//탈퇴처리
		@ResponseBody //JSON으로 응답받기 위한 annotation
		@GetMapping("/ajax/member/delete-me")
		public AjaxResponse doDeleteMe(HttpSession session, 
				@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
			//현재 로그인되어있는 사용자의 정보
//			MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_"); //바로 위위줄 @Session~ 코드와 동일
			
			boolean isSuccess = this.memberService.deleteMe(memberVO.getEmail());
			
			if(isSuccess) {
				session.invalidate();
			}
			
			return new AjaxResponse().append("next", 
							isSuccess ? "/member/success-delete-me"
									: "/member/fail-delete-me");
		}
		
		
		@GetMapping("/member/{result}-delete-me")
			public String viewDeleteMePage(@PathVariable String result) {
				result = result.toLowerCase();
				if ( !result.equals("fail") && !result.equals("success") ) {
					// result의 값이 fail, success가 아니면 404페이지 보여주기
					return "error/404";
				}
				return "member/" + result + "deleteme";
			}
}

