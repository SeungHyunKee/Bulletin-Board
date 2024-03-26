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
	
	
	/**
	 * 파라미터로 전달받은 게시글 ID의 조회수를 1 증가시킴
	 *@prarm id 게시글 ID(번호)
	 * @return DB에 update 한 개수
	 */
	public int increaseViewCount(int id);

	
	/**
	 * 파라미터로 전달받은 게시글 ID의 게시글 정보 조회
	 * @param id 게시글 ID(번호)
	 * @return
	 */
	public BoardVO getOneBoard(int id);

	/**
	 * DB에 <게시글의 정보를 수정>한다. -> 게시글 수정을 위한 메소드 정의
	 * BoardVO의 id값에 수정할 게시글의 id값이 있어야한다
	 * @param boardVO 사용자가 수정한 게시글의 정보
	 * @return DB에 업데이트한 게시글의 수
	 */

	public int createNewBoard(BoardVO boardVO);
}
