package com.hello.forum.member.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.hello.forum.member.vo.MemberVO;

@MybatisTest //Mybatis framework테스트를 위해 설정
//실제 db에서 테스트하기위한 설정
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
//MemberDaoImpl 테스트를 위해 MemberDaoImpl을 Import
@Import(MemberDaoImpl.class)
public class MemberDaoImplTest {

	@Autowired
	private MemberDao memberDao;
	
	@Test
	@DisplayName("이메일 개수 확인 테스트")
	public void getEmailCountTest() {
		int count = this.memberDao.getEmailCount("testuser@testuser.com");
		Assertions.assertEquals(count, 0); //count가 0과 같은가?
		
		count = this.memberDao.getEmailCount("dd@naver.com");
		Assertions.assertEquals(count, 1); 
	}
	
	@Test
	@DisplayName("회원가입 실패 테스트")
	public void createNewMemberFailTest() {
		
		MemberVO memberVO = new MemberVO();
//		int insertedCount = this.memberDao.createNewMember(memberVO);
		
		DataIntegrityViolationException exception = Assertions
				.assertThrows(DataIntegrityViolationException.class, 
				() -> this.memberDao.createNewMember(memberVO));
		
		Assertions.assertNotNull(exception);	
	}
	
	//이메일이 중복이 되는경우에 대한 테스트. insert query에 있는 정보 다 적어줌
	@Test
	@DisplayName("회원가입 실패 테스트 - 중복된 이메일이 있을경우")
	public void createNewMemberFailDupEmailTest() {
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("dd@naver.com");
		memberVO.setName("junit테스트");
		memberVO.setPassword("testpassword for junit");
		memberVO.setSalt("asdfasdf");
		
		DuplicateKeyException exception = Assertions.assertThrows(
					DuplicateKeyException.class,
					() -> this.memberDao.createNewMember(memberVO));
		Assertions.assertNotNull(exception);
	}
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void createNewMemberSuccesslTest() {
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("junit-test-email-exam@system.com");
		memberVO.setName("junit테스트");
		memberVO.setPassword("testpassword for junit");
		memberVO.setSalt("asdfasdf");
		
		int insertedCount = this.memberDao.createNewMember(memberVO);
		Assertions.assertEquals(insertedCount, 1);
	}
}
