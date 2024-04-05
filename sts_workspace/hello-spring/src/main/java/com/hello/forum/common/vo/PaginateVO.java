package com.hello.forum.common.vo;

public class PaginateVO {


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

	/**
	 * 한페이지 그룹에 보여줄 페이지 번호의 개수
	 */
	private int pageCountInGroup;
	
	/**
	 * 총페이지 그룹의 개수
	 */
	private int groupCount;
	
	/**
	 * 현재페이지 그룹번호
	 */
	private int groupNo;
	
	/**
	 * 현재페이지그룹번호의 시작페이지 번호
	 */
	private int groupStartPageNo;
	
	/**
	 * 현재페이지 그룹번호의 끝페이지 번호
	 */
	private int groupEndPageNo;
	
	/**
	 * 다음그룹이 존재하는지 확인
	 */
	private boolean hasNextGroup;
	
	/**
	 * 이전그룹이 존재하는지 확인
	 */
	private boolean hasPrevGroup;
	
	/**
	 * 다음그룹의 시작페이지 번호
	 */
	private int nextGroupStartPageNo;
	
	/**
	 * 이전그룹의 시작페이지번호
	 */
	private int prevGroupStartPageNo;
	
	
	public int getPageCountInGroup() {
		return pageCountInGroup;
	}

	public void setPageCountInGroup(int pageCountInGroup) {
		this.pageCountInGroup = pageCountInGroup;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public int getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public int getGroupStartPageNo() {
		return groupStartPageNo;
	}

	public void setGroupStartPageNo(int groupStartPageNo) {
		this.groupStartPageNo = groupStartPageNo;
	}

	public int getGroupEndPageNo() {
		return groupEndPageNo;
	}

	public void setGroupEndPageNo(int groupEndPageNo) {
		this.groupEndPageNo = groupEndPageNo;
	}

	public boolean isHasNextGroup() {
		return hasNextGroup;
	}

	public void setHasNextGroup(boolean hasNextGroup) {
		this.hasNextGroup = hasNextGroup;
	}

	public boolean isHasPrevGroup() {
		return hasPrevGroup;
	}

	public void setHasPrevGroup(boolean hasPrevGroup) {
		this.hasPrevGroup = hasPrevGroup;
	}

	public int getNextGroupStartPageNo() {
		return nextGroupStartPageNo;
	}

	public void setNextGroupStartPageNo(int nextGroupStartPageNo) {
		this.nextGroupStartPageNo = nextGroupStartPageNo;
	}

	public int getPrevGroupStartPageNo() {
		return prevGroupStartPageNo;
	}

	public void setPrevGroupStartPageNo(int prevGroupStartPageNo) {
		this.prevGroupStartPageNo = prevGroupStartPageNo;
	}

	public PaginateVO() {
		listSize = 10;
		pageCountInGroup = 10;
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

	
	
	
	/**
	 * 전체게시글의 수를 넣어주면 알아서 계산해서 보여줌 : pageCount, groupCount, groupNo, 시작번호끝번호 ...
	 * 계산된결과를 가지고 jsp에서 출력만 해주면 됨!!
	 * @param listCount
	 */
	public void setPageCount(int listCount) {
		//전체 게시글을 listsize로 나누었을때 몇개의 페이지가 생길지 계산
		this.pageCount = (int) Math.ceil((double) listCount / this.listSize);
	
		//전체페이지(총페이지 그룹의 개수)를 pagecountInGroup(한페이지그룹에 보여줄 페이지번호 개수)으로 나누었을때
		//몇개의 페이지그룹이 생길지 계산
		this.groupCount = (int) Math.ceil((double)this.pageCount / this.pageCountInGroup);
	
		//현재보고있는 페이지번호가 몇번페이지그룹에있는지 계산
		this.groupNo = this.pageNo / this.pageCountInGroup;
		
		//현재페이지그룹의 시작페이지번호를 꼐산
		this.groupStartPageNo = this.groupNo * this.pageCountInGroup;
		//현재페이ㅣ그룹의 끝페이지 번호를 계산
		this.groupEndPageNo = (this.groupNo + 1 ) * this.pageCountInGroup;
	
		//그룹의 끝페이지번호가 전체페이지번호보다 클 때, 그룹의 끝페이지번호를 전체 페이지번호로 조정
		//현재페이지 그룹번호의 끝페이지 번호 >= 현재 페이지 수 
		if (this.groupEndPageNo >= this.pageCount) { 
			this.groupEndPageNo = this.pageCount -1 ; 
		}
		
		//검색된 결과가 없더라도 pagination이 잘 나옴
		if(this.groupEndPageNo < 0) {
			this.groupEndPageNo = 0;
		}
		
		//현재그룹에서 다음그룹이 존재하는지 계산
		this.hasNextGroup = this.groupNo + 1 < this.groupCount;
		//현재그룹에서 이전그룹이 존재하는지 계산
		this.hasPrevGroup = this.groupNo > 0;
		//다음그룹의 시작페이지번호 계산
		this.nextGroupStartPageNo = this.groupEndPageNo + 1;
		//이전그룹의 시작페이지번호 계산
		this.prevGroupStartPageNo = this.groupStartPageNo - this.pageCountInGroup;
	
	}
}
