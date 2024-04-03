package com.hello.forum.bbs.dao;

import java.util.List;

import com.hello.forum.bbs.vo.ReplyVO;

public interface ReplyDao {

	/**
	 * 게시글의 모든댓글 조회
	 * @param boardId 조회할게시글 번호
	 * @return 게시글에 등록된 모든댓글 목록
	 */
	public List<ReplyVO> getAllReplies(int boardId);
	
	/**
	 * 댓글 하나 조회
	 * @param replyId 조회할 댓글 번호
	 * @return reply에 해당하는 댓글 정보
	 */
	public ReplyVO getOneReply(int replyId);
	
	/**
	 * 게시글에 댓글 등록
	 * @param replyVO등록할 댓글의 정보
	 * @return 댓글을 등록한 개수
	 */
	public int createNewReply(ReplyVO replyVO);

	/**
	 * 댓글 하나 삭제
	 * @param replyId 삭제할 댓글의 번호
	 * @return 댓글을 삭제한 개수
	 */
	public int deleteOneReply(int replyId);

	/**
	 * 댓글 내용 수정
	 * @param replyVO 수정할 댓글 정보
	 * @return 수정된 댓글의 개수
	 */
	public int modifyOneReply(ReplyVO replyVO);
	
	/**
	 * 댓글의 추천수 1 증가시킴
	 * @param replyId 추천수를 1 증가시킬 댓글번호
	 * @return 추천한 댓글의 개수
	 */
	public int recommendOneReply(int replyId);
}
