package com.hello.forum.bbs.service;

import java.util.List;

import com.hello.forum.bbs.vo.ReplyVO;

public interface ReplyService {

	/**
	 * 게시글의 모든 댓글 조회
	 * @param boardId 조회할 게시글의 번호
	 * @return 게시글에 등록된 모든 댓글 목록
	 */
	public List<ReplyVO> getAllReplies(int boardId);
	
	/**
	 * 게시글에 댓글 등록
	 * @param replyVO 등록할 댓글의 정보
	 * @return 댓글 등록한 개수
	 */
	public boolean createNewReply(ReplyVO replyVO);
	
	/**
	 * 댓글 하나를 삭제한다
	 * @param replyId 삭제할 댓글 번호
	 * @param email 삭제요청한 사용자 이메일
	 * @return 댓글삭제성공여부
	 */
	public boolean deleteOneReply(int replyId, String email);
	
	/**
	 * 댓글 내용을 수정
	 * @param replyVO 수정할 댓그의 정보
	 * @return 댓글수정성공여부
	 */
	public boolean modifyOneReply(ReplyVO replyVO);
	
	/**
	 * 댓글의 추천수 1 증가시킴
	 * @param replyId 추천수를 1 증가시킬 댓글번호
	 * @param email 삭제를 요청한 사용자의 이메일
	 * @return 댓글 추천 성공여부
	 */
	public boolean recommendOneReply(int replyId, String email);
}
