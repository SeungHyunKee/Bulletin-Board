package com.hello.forum.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

/**
 * 게시글 목록 조회 요청을 처리하기 위한 controller
 */
@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/list")
	public ModelAndView viewBoardList() {
		BoardListVO boardListVO = boardService.getAllBoard();
	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("board/boardlist");
		modelAndView.addObject("boardList", boardListVO);
		return modelAndView;
	}
	
	// 게시글 작성을 위한 화면 만들기
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}
	
	/*
	 * 게시글 작성(=등록) 요청처리를 위한 메소드 작성 
	 */
	@PostMapping("/board/write")
	public ModelAndView doBoardWrite(@ModelAttribute BoardVO boardVO) {
		System.out.println("제목: " + boardVO.getSubject());
		System.out.println("이메일: " + boardVO.getEmail());
		System.out.println("내용: " + boardVO.getContent());
		System.out.println("등록일: " + boardVO.getCrtDt());
		System.out.println("수정일: " + boardVO.getMdfyDt());
		System.out.println("FileName: " + boardVO.getFileName());
		System.out.println("OriginFileName: " + boardVO.getOriginFileName());

		ModelAndView modelAndView = new ModelAndView();
		
		//게시글 등록
		boolean isSuccess = boardService.createNewBoard(boardVO);
		if(isSuccess) {
			//게시글등록결과 성공 -> /board/list URL 로 이동
			modelAndView.setViewName("redirect:/board/list");
			return modelAndView;
		}
		else { //게시글등록결과 실패라면 : 게시글등록(작성) 화면으로 데이터 보내주고,해당화면에서 boardVO값으로 등록값을 설정
			modelAndView.setViewName("/board/boardwrite");
			modelAndView.addObject("boardVO", boardVO);
			return modelAndView;
		}
	}
	
	// 게시글 조회 요청 처리를 위한 메소드
	@GetMapping("/board/view")
	public ModelAndView viewOneBoard(@RequestParam int id) { 
		//@RequestParam int id : URL로 전달한 파라미터를 받아올 수 있는방법
		// -> URL뒤에 ?기호로 시작하는 파라미터 : Query String Parameter
		BoardVO boardVO = boardService.getOneBoard(id, true);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("board/boardview");
		modelAndView.addObject("boardVO", boardVO);
		return modelAndView;
	}
	
	// 게시글 수정을 위한 화면 만들기
	@GetMapping("/board/modify/{id}")
	public ModelAndView viewBoardModifyPage(@PathVariable int id) {
		//게시글 수정을 위해 게시글의 내용 조회
		// 게시글 조회와 동일한 코드 호출
		BoardVO boardVO = boardService.getOneBoard(id, false);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("board/boardview");
		modelAndView.addObject("boardVO", boardVO);
		return modelAndView;
	}
	
	
	//게시글 수정 요청처리를 위한 메소드
	@PostMapping("/board/modify")
	public ModelAndView doBoardUpdate(@ModelAttribute BoardVO boardVO) {
		System.out.println("ID: " + boardVO.getId());
		System.out.println("제목: " + boardVO.getSubject());
		System.out.println("이메일: " + boardVO.getEmail());
		System.out.println("내용: " + boardVO.getContent());
		System.out.println("등록일: " + boardVO.getCrtDt());
		System.out.println("수정일: " + boardVO.getMdfyDt());
		System.out.println("FileName: " + boardVO.getFileName());
		System.out.println("OriginFileName: " + boardVO.getOriginFileName());
		
		ModelAndView modelAndView = new ModelAndView();
		
		// 게시글 수정
		boolean isSuccess = boardService.updateOneBoard(boardVO);
		if (isSuccess) {
			// 게시글 수정 결과가 성공이라면
			// /board/view?id=id URL로 이동한다.
			modelAndView.setViewName("redirect:/board/view?id=" + boardVO.getId());
			return modelAndView;
		}
		else {
			// 게시글 수정 결과가 실패라면
			// 게시글 수정 화면으로 데이터를 보내준다.
			modelAndView.setViewName("board/boardmodify");
			modelAndView.addObject("boardVO", boardVO);
			return modelAndView;
			}
		}

}
