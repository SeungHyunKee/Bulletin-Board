package com.hello.forum.member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hello.forum.member.vo.MemberVO;

//MEMBERS테이블에 등록을 위한 MemberDaoImpl
@Repository
public class MemberDaoImpl extends SqlSessionDaoSupport implements MemberDao {

	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public int getEmailCount(String email) {
		return getSqlSession().selectOne("getEmailCount", email);
	}

	@Override
	public int createNewMember(MemberVO memberVO) {
		return getSqlSession().insert("createNewMember", memberVO);
	}

	@Override
	public String selectSalt(String email) {
		return getSqlSession().selectOne("selectSalt", email);
	}

	@Override
	public MemberVO selectMemberByEmailAndPassword(MemberVO memberVO) {
		return getSqlSession().selectOne("selectMemberByEmailAndPassword", memberVO);
	}
	
}
