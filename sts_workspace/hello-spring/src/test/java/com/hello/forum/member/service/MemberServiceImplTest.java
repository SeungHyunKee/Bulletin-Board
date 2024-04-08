package com.hello.forum.member.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hello.forum.beans.SHA;
import com.hello.forum.exceptions.AlreadyUseException;
import com.hello.forum.exceptions.UserIdentifyNotMatchException;
import com.hello.forum.member.dao.MemberDao;
import com.hello.forum.member.dao.MemberDaoImpl;
import com.hello.forum.member.vo.MemberVO;

@SpringBootTest //모든 junit 테스트를 위해 필요한 bean을 가져오기 위한 annotation
@ExtendWith(SpringExtension.class) // Spring Test를 위해서 junit5를 사용하기위한 설정 (junit4나 그 이하는 이 설정 안해줘도됨)
// @Autowired에 있는거는 다 적어줌
//MemberService를 테스트하기위해 import
// MemberService이 동작하기위해 MemberDaoImpl을 import
//MemberService이 동작하기위해 SHA를 import
@Import({ MemberServiceImpl.class, MemberDaoImpl.class, SHA.class })
public class MemberServiceImplTest {
	
	/**
	 * @Import 해 온 MemberServiceImpl을 주입한다
	 * - 진짜 테스트하고싶은것 : @Autowired
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * MemberServiceImpl에 DI해주기 위한 MemberDao선언
	 */
	@MockBean
	private MemberDao memberDao;
	
	/**
	 * MembesrServiceImpl에 DI해주기위한 SHA 선언
	 */
//	@MockBean
//	private SHA sha;
	
	//test는 모두 void를 이용함
	@Test // 이 어노테이션이 붙어있는것이 jnuit이 테스트 시킬 대상임
	@DisplayName("회원ID 중복체크 테스트") // 테스트에 대한 설명	
	public void checkAvailableEmailTest() {
		
		//MemberServiceImpl의 checkAvailableEmail(email)함수를 테스트하기 위해서
		//MemberDaoImpl의 getEmailCount(email)이 동작해야하는 방법을 작성한다
		// -> 이걸 jnuit에서 : Given이라고 한다
		
		// 1.Given
		BDDMockito.given(this.memberDao.getEmailCount("user01@gmail.com")).willReturn(0);
		// MemberServiceImpl이 memberDao.getEmailCount에 "user01@gmail.com"을 전달하면 0을 반환시켜라
		// 															0 : 이메일이 db에 등록안되어있다고 가정
		// 서비스의 논리적, 물리적 오류가 있는지를 발견하고 싶은것!
		
		// 2. Given 
		// 															1 : 이메일이 db에 등록되어있다고 가정
		BDDMockito.given(this.memberDao.getEmailCount("user02@gmail.com")).willReturn(1);
		
		//3. when 
		// given에서 정의했던 값들 파라미터로 던져줌
		boolean isAvailableEmail = this.memberService
										.checkAvailableEmail("user01@gmail.com"); //예상값 :true
		//4. then
		//isAvailableEmail의 값이 true면 성공 / 아니라면 실패
		Assertions.assertTrue(isAvailableEmail);
		
		// 5. when
		isAvailableEmail = this.memberService
				.checkAvailableEmail("user02@gmail.com");
		Assertions.assertFalse(isAvailableEmail);
		
		//6.Verify 검증
		// Given으로 준 것이 의도하는대로 검증되었는지?!
		Mockito.verify(this.memberDao).getEmailCount("user01@gmail.com");
		Mockito.verify(this.memberDao).getEmailCount("user02@gmail.com");

		// when, then, verify 가 전부 성공해야 이 테스트가 성공함
		// 만약 junit테스트에서 에러가 발생하면, 여기 코드가 아닌, 해당 파일의 코드를 수정해야함
	}
		
	@Test
	@DisplayName("회원가입 실패 테스트")
	public void createNewMemberFailTest() {
		//1.Given
		BDDMockito.given(this.memberDao.getEmailCount("user01@gmail.com"))
				.willReturn(1);
		//2.when
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		
		//작성된 테스트코드는 AlreadyUseException이 발생해야한다!!
//		boolean isSuccess = this.memberService.createNewMember(memberVO);
		AlreadyUseException exception = Assertions.assertThrows( //assertThrows는 발생한예외를 받아올수있음
				AlreadyUseException.class, 
				() -> this.memberService.createNewMember(memberVO)) ;//예외가 발생하는가? -> 그럼 정상이다!
		//3. then
		// 예상되는 예외메시지와 실제발생된 예외의 메시지가 같은지 비교
		String message = "이미 사용중인 이메일입니다.";
		Assertions.assertEquals(message, exception.getMessage());
		
		// 4. verify
		Mockito.verify(this.memberDao).getEmailCount("user01@gmail.com");
	}
	
	@Test
	@DisplayName("회원가입 성공테스트")
	public void createNewMemberSuccessTest() {
		
		//1. Given
		BDDMockito.given(this.memberDao.getEmailCount("user01@gmail.com"))
		.willReturn(0);
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setName("테스트 사용자");
		memberVO.setPassword("testpassword");
		memberVO.setConfirmPassword("testpassword");
		
		BDDMockito.given(this.memberDao.createNewMember(memberVO))
		.willReturn(1);
		
		//2. When
		boolean isSuccess = this.memberService.createNewMember(memberVO);
		//     isSuccess=true;
		
		//3. Then
		Assertions.assertTrue(isSuccess); //회원가입의결과가 true인지 검증
		Assertions.assertNotNull(memberVO.getSalt()); //비밀번호 암호화를 위한 salt가 생성되었는지 검증
		//비밀번호가 암호화되어 confirmPassword와 다른지 검증
		Assertions.assertNotEquals(memberVO.getPassword(),
									memberVO.getConfirmPassword());
		
		//4.Verify
		Mockito.verify(this.memberDao).getEmailCount("user01@gmail.com");
		Mockito.verify(this.memberDao).createNewMember(memberVO);
	}
	
	// 1. exception 발생하는 실패 case
	// 2. if null 일 때 실패 case 
	// 3. 1번 2번 둘다 아닐때 성공적으로 값이 반환되는가 - 성공case
	@Test 
	@DisplayName("회원가입실패테스트 1 -> salt가 없는 케이스")
	public void getMemberSaltFailTest() {
		//1.Given
		BDDMockito.given(this.memberDao.selectSalt("user01@gmail.com"))
		.willReturn(null);
		
		//2.when
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setPassword("testpassword");
		
		UserIdentifyNotMatchException exception = Assertions.assertThrows( 
				UserIdentifyNotMatchException.class, 
				() -> this.memberService.getMember(memberVO)) ;
		
		//3.then
		Assertions.assertNotNull(exception);
		String message = "이메일 또는 비밀번호가 일치하지 않습니다.";
		Assertions.assertEquals(message, exception.getMessage());
		
		//4.verify
		Mockito.verify(this.memberDao).selectSalt("user01@gmail.com");

	}
	
	
	
	@Test 
	@DisplayName("회원정보 null exception 테스트")
	public void getMemberInfoFailTest() {
		//1.Given
		BDDMockito.given(this.memberDao.selectSalt("user01@gmail.com"))
		.willReturn("asdfg");
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setPassword("testpassword");
		
		BDDMockito
		.given(this.memberDao.selectMemberByEmailAndPassword(memberVO))
		.willReturn(null);
		
		//2.when
		UserIdentifyNotMatchException exception = Assertions.assertThrows( 
				UserIdentifyNotMatchException.class, 
				() -> this.memberService.getMember(memberVO)) ;
		
		//3.then
		String message = "이메일 또는 비밀번호가 일치하지 않습니다.";
		Assertions.assertEquals(message, exception.getMessage());
		
		//4.verify
		Mockito.verify(this.memberDao).selectSalt("user01@gmail.com");
		Mockito.verify(this.memberDao).selectMemberByEmailAndPassword(memberVO);

	}
	
	
	@Test 
	@DisplayName("회원가입 성공 테스트")
	public void getMemberSuccessTest() {
		//1.Given
		BDDMockito.given(this.memberDao.selectSalt("user01@gmail.com"))
		.willReturn("uabcds");
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		memberVO.setPassword("testpassword");
		
		BDDMockito
		.given(this.memberDao.selectMemberByEmailAndPassword(memberVO))
		.willReturn(memberVO);
		
		
		//2.when
		MemberVO loginMemberVO = this.memberService.getMember(memberVO);
		
		//3.then
		Assertions.assertNotNull(loginMemberVO);
		Assertions.assertEquals(memberVO.getEmail(), loginMemberVO.getEmail());
		Assertions.assertNotEquals(loginMemberVO.getPassword(), "testpassword");

		
		//4.verify
		Mockito.verify(this.memberDao).selectSalt("user01@gmail.com");
		Mockito.verify(this.memberDao).selectMemberByEmailAndPassword(memberVO);
	}
	
	
	@Test 
	@DisplayName("회원 탈퇴 실패 테스트")
	public void deleteMeFailTest() {
		//1.Given
		BDDMockito.given(this.memberDao.deleteMemberByEmail("user01@gmail.com"))
		.willReturn(0); //0보다 크면 성공이므로
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		
		//2.when
		boolean isSuccess = this.memberService.deleteMe("user01@gmail.com");
		
		//3.then
		Assertions.assertFalse(isSuccess);
		
		//4.verify
		Mockito.verify(this.memberDao).deleteMemberByEmail("user01@gmail.com");
	}
	
	
	@Test 
	@DisplayName("회원 탈퇴 성공 테스트")
	public void deleteMeSuccessTest() {
		//1.Given
		BDDMockito.given(this.memberDao.deleteMemberByEmail("user01@gmail.com"))
		.willReturn(1); //0보다 크면 성공이므로
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user01@gmail.com");
		
		//2.when
		boolean isSuccess = this.memberService.deleteMe("user01@gmail.com");
		
		//3.then
		Assertions.assertTrue(isSuccess);
		
		//4.verify
		Mockito.verify(this.memberDao).deleteMemberByEmail("user01@gmail.com");
	}
	
	
}

	
	


