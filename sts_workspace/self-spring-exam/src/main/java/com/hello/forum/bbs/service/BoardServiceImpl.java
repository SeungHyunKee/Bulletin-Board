package com.hello.forum.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hello.forum.bbs.dao.BoardDao;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

/**
 * 게시글 목록을 조회하기위한 service 인터페이스 생성
 */
@Service
public class BoardServiceImpl implements BoardService{
	
	/*
	 * Bean Container에 등록된 boardDao Bean을 가져와 주입시킴
	 */
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public BoardListVO getAllBoard() {
		//게시글 건수와 게시글 목록 가지는 VO객체 선언
		BoardListVO boardListVO = new BoardListVO();
		// 게시글 총 건수 조회
		boardListVO.setBoardCnt(boardDao.getBoardAllCount());
		// 게시글 목록 조회
		boardListVO.setBoardList(boardDao.getAllBoard());
		return boardListVO;
	}
	
	public boolean createNewBoard(BoardVO boardVO) {
		//DB에 게시글 등록
		// createCount에는 DB에 등록한 게시글의 개수를 반환
		int createCount = boardDao.createNewBoard(boardVO);
		//DB에 등록한 개수가 0보다크면 성공. 아님 실패
		return createCount > 0;
	}
	
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		if (isIncrease) {
			// 파라미터로 전달받은 게시글의 조회 수 증가
			// updateCount에는 DB에 업데이트한 게시글의 수를 반환.
			int updateCount = boardDao.increaseViewCount(id);
			if (updateCount == 0) {
				// updateCount가 0이라는 것은 
				// 파라미터로 전달받은 id 값이 DB에 존재하지 않는다는 의미이다.
				// 이 경우, 잘못된 접근입니다. 라고 사용자에게 예외 메시지를 보내준다.
				throw new IllegalArgumentException("잘못된 접근입니다.");
			}
		}

		//파라미터로 전달받은 게시글의 조회수 증가
		//updateCount에는 DB에 업데이트한 게시글의 수 반환
		int updateCount = boardDao.increaseViewCount(id);
		if(updateCount==0) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		//예외발생안했다면, 게시글정보 조회
		BoardVO boardVO = boardDao.getOneBoard(id);
		if(boardVO == null) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		return boardVO;
	}
	
	// 게시글 수정을 위한 메소드 구현
	@Override
	public boolean updateOneBoard(BoardVO boardVO) {
		int updateCount = boardDao.updateOneBoard(boardVO);
		return updateCount > 0;
	}
		
	
	
	

}
