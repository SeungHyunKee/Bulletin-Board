package com.hello.forum.member.dao;

import com.hello.forum.member.vo.MemberVO;

// MEMBERS 테이블에 등록을 위한 MemberDao 인터페이스
public interface MemberDao {

	/**
	 * 파라미터로 전달된 이메일이 DB에 몇건 존재하는지 확인
	 * @param email 사용자가 가입요청한 이메일
	 * @return 동일한 이메일로 등록된 회원의 수
	 */
	public int getEmailCount(String email);
	
	/**
	 * 회원가입 쿼리를 실행한다
	 * @param memberVO 사용자가 입력한 회원정보
	 * @return DB에 insert 한 회원의 개수
	 */
	public int createNewMember(MemberVO memberVO);

	public String selectSalt(String email);

	public MemberVO selectMemberByEmailAndPassword(MemberVO memberVO);
}
