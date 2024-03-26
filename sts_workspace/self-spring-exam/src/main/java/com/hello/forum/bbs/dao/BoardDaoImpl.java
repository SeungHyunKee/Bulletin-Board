package com.hello.forum.bbs.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hello.forum.bbs.vo.BoardVO;

/**
 * BoardDao인터페이스를 구현한 BoardDaoImpl클래스
 */
@Repository
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {
// sqlSessionDaoSupport : myBatis 내장클래스로, myBatis가 쿼리를 호출하고 결과를 받아오는 코드
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int getBoardAllCount() {
		return getSqlSession().selectOne("getBoardAllCount");
	}

	@Override
	public List<BoardVO> getAllBoard() {
		return getSqlSession().selectList("getAllBoard");
	}

	@Override
	public int increaseViewCount(int id) {
		return getSqlSession().update("increaseViewCount", id);
	}

	@Override
	public BoardVO getOneBoard(int id) {
		return getSqlSession().selectOne("getOneBoard", id);
	}
	
	// 게시글 수정을 위한 메소드 구현
	public int updateOneBoard(BoardVO boardVO) {
		return getSqlSession().selectOne("updateOneBoard", boardVO);
	}

	@Override
	public int createNewBoard(BoardVO boardVO) {
		// TODO Auto-generated method stub
		return 0;
	}
}
