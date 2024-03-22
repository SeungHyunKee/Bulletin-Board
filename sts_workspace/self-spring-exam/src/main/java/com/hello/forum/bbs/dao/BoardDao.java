package com.hello.forum.bbs.dao;

import java.util.List;

import com.hello.forum.bbs.vo.BoardVO;

/**
 * 게시글의 총 개수와 게시글 목록 조회를 위한 Dao 인터페이스
 */
public interface BoardDao {

	/**
	 * DB에 저장된 모든 게시글의 수 조회
	 * @return
	 */
	public int getBoardAllCount();
	
	/**
	 * DB에 저장된 모든 게시글의 목록 조회
	 * @return
	 */
	public List<BoardVO> getAllBoard();
}
