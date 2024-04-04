package com.hello.forum.bbs.vo;

public class SearchBoardVO {

	/**
	 * 검색할 페이지 번호
	 */
	private int pageNo;
	
	/**
	 * 한 목록에 노출시킬 게시글의 개수
	 */
	private int listSize;
	
	/**
	 * 생성할 최대 페이지 수 
	 */
	private int pageCount;

	
	public SearchBoardVO() {
		listSize = 10;
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int listCount) {
		this.pageCount = (int) Math.ceil((double) listCount / this.listSize);
	}
	
	
}
