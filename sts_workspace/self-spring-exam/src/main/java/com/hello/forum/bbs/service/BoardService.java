package com.hello.forum.bbs.service;

import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

/**
 * 게시글 목록을 조회하기 위한 BoardService인터페이스
 */
public interface BoardService {

	/**
	 * 게시글의 목록과 게시글의 건수 모두 조회
	 * @return
	 */
	public BoardListVO getAllBoard();
	
	/**
	 * 새로운 게시글 등록(작성)처리한다
	 * @param board 사용자가 입력한 게시글 정보
	 * @ return 정상적으로 등록되었는지 여부
	 */
	public boolean createNewBoard(BoardVO boardVO);
	
	/** <게시글 조회를 위한 메소드>
	 * 파라미터로 전달받은 id로 게시글을 조회 : 게시글 조회시 조회수도 1 증가함
	 * @param id 조회할 게시글의 ID
	 * @param isIncrease 값이 true면 조회수를 증가시킴
	 * @return 게시글 정보
	 */
	public BoardVO getOneBoard(int id, boolean isIncrease);
	
	/**
	 * 게시글 정보 수정
	 * BoardVO의 id값에 수정할 게시글의 id값 있어야함
	 * @param boardVO boardVO사용자가 수정한 게시글의 정보
	 * @return 정상적으로 수정되었는지 여부
	 */
	public boolean updateOneBoard(BoardVO boardVO);
}
