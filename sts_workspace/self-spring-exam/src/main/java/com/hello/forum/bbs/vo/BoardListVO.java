package com.hello.forum.bbs.vo;

import java.util.List;

/**
 * 게시글의 총 개수와 게시글 목록을 가지는 BoardListVO
 */
public class BoardListVO {

	// boardCnt = 게시글 총 개수
	private int boardCnt;
	// boardList= 게시글 목록
	private List<BoardVO> boardList;
	
	
	public int getBoardCnt() {
		return boardCnt;
	}
	public void setBoardCnt(int boardCnt) {
		this.boardCnt = boardCnt;
	}
	public List<BoardVO> getBoardList() {
		return boardList;
	}
	public void setBoardList(List<BoardVO> boardList) {
		this.boardList = boardList;
	}
	
	
}
