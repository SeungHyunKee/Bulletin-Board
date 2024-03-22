package com.hello.forum.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

@Controller
public class BoardControllerr {

	/*
	 * Bean Container 에서 BoardService타입객체를 찾아
	 * 아래 멤버변수에게 할당한다(DI)
	 */
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/list")
	public String viewBoardListPage(Model model) {
		
		// 1. 게시글의 건수와 게시글의 목록을 조회해서
		BoardListVO boardListVO = this.boardService.getAllBoard();
		
		// 2. /WEB-INF/vies/board/boardlist.jsp페이지에게 게시글의 건수와 게시글의 목록을 전달하고
		model.addAttribute("boardList", boardListVO);
		
		// 3.화면을 보여준다
		return "board/boardlist";
	}
	
	/**
	 * 게시글 작성 페이지를 보여주는 URL
	 * @return
	 */
	@GetMapping("/board/write") //브라우저에서 링크를 클릭, 브라우저 url을 직접 입력
	public String viewBoardWritePage(){
		return "board/boardwrite";
	}
	
	
	//페이지를 보여주는 url :view~
	// 처리를 하는 url : do~
	/**
	 * 스프링 애플리케이션을 개발할 때 원래는 같은 url을 정의할 수 없다.
	 * 하지만, 예외적으로 허용되는 경우 : mapping앞 메소드가 달라지는 경우
	 * (Get/board/write), (Post/board/write) 이므로 메소드명이 다르므로 사용가능
	 * 
	 * 해야될것 : 글등록 페이지에서 게시글작성하고 '저장' 버튼 클릭하면 -> 데이터베이스에 글정보를 저장(insert)해야한다
	 * insert를 위해서는 사용자가 작성한 글 정보를 알아야한다.
	 * 
	 * 파라미터를 받아오는 3가지 방법
	 * 방법1> Servlet like (HttpServletRequest 객체를 통해서 받아옴)
	 * 방법2> @REqiestParam : servletlike를 좀더 편하게 사용
	 * 방법3> Command Object : 보편적으로 많이 사용하는 방법 > 파라미터 처리가 매우 편하다!!!
	 * 방법4> @pathvariable
	 * 
	 * @return
	 */
	@PostMapping("/board/write") // 요청이 post일때 사용하는 annotation
	public String doBoardWrite(/*Spring이 알맞은 파라미터를 자동으로 보내준다. servlet like: HttpServletRequest request*/
		/*컨트롤러로 전송된 파라미터를 하나씩 받아오는 방법 @RequestParam
		 * @RequestParam으로 정의된 파라미터는 필수파라미터!!
		 * 컨트롤러로 전송되는 파라미터의 개수가 몇개 없을 때, (예. 3개미만) 사용
		 * 
		 * */
//			@RequestParam String subject,
//			@RequestParam String email,
//			@RequestParam String content
			
			/*Comand Object
			 * 파라미터로 전송된 이름과 BoardVO의 멤버변수의 이름과 같은것이 있다면
			 * 해당 멤버변수에 파라미터의 값을 할당해준다(Setter 이용) - 값을 자동으로 넣어준다*/
			BoardVO boardVO, //사용자가 데이터를 안보내주면 여기엔 데이터가 없는것
			@RequestParam MultipartFile file
			) {
		
		/*
		* Servlet Like
		- HttpServletRequest 를 이용
		- Interceptor에서 이용
		- Filter 에서 이용
		*/
		System.out.println("글등록 처리를 해야됩니다.");
//		String subject = request.getParameter("subject");
//		String email = request.getParameter("email");
//		String content = request.getParameter("content");
//		
//		System.out.println("제목: "+ subject);
//		System.out.println("이메일: "+ email);
//		System.out.println("내용: "+ content);

//		System.out.println("제목: " + boardVO.getSubject());
//		System.out.println("이메일: " + boardVO.getEmail());
//		System.out.println("내용: " + boardVO.getContent());

		boolean isCreateSuccess = this.boardService.createNewBoard(boardVO, file);
		if (isCreateSuccess) {
			System.out.println("글 등록 성공!");
		}
		else {
			System.out.println("글 등록 실패!");
		}
		
		// board/boardlist페이지를 보여주는 URL로 이동처리.
		// redirect:/board/list => 스프링은 브라우저에게 /board/list로 이동하라는 명령을 전송
		// 명령을 받은 브라우저는 /board/list로 url을 이동시킨다
		// 그 이후에 /board/list로 브라우저가 요청을 하게되면
		// 스프링 컨트롤러에서 /board/list URL에 알맞은 처리를 진행한다
		return "redirect:/board/list";
	}
	
	// browser에서 url을 http://localhost:8080/board/view?id=1  <-- 나쁘지 않은 방법
	// browser에서 url을 http://localhost:8080/board/view/1  <--url자체가 파라미터가 되는 방법

	// url뒤에 ? <-- 쿼리 파라미터(조회하는 파라미터. 여러가지 데이터를 받을때는 &사용해서 받을 수 있음)
	// ?id=1 <--parameter key : id, parameter value : 1
	// ?id=1&subject=abc <-- parameter key: id, parameter value: 1/ parameter abc
	@GetMapping("/board/view")
	public String viewBoardDetailPage(@RequestParam int id, Model model) {

		//1. boardService에게 파라미터로 전달받은 id값을 보내준다
		//2.boardService는 파라미터로 전달받은 id의 게시글정보를 조회해서 반환해주면
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		
		//3.boardview 페이지에 데이터를 전송해준다
		model.addAttribute("boardVO", boardVO);
		
		//4. 화면을 보여준다
		return "board/boardview";
	}
	
	@GetMapping("/board/modify/{id}") //board/modify/1 <-- id변수의 값은 1
	public String viewBoardModifyPade( @PathVariable int id, Model model) {
		//1. 전달받은 id의값으로 게시글을 조회한다
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		//2. 게시글의 정보를 화면에 보내준다
		model.addAttribute("boardVO", boardVO);
		
		//3. 화면을 보여준다
		return "board/boardmodify";
	}
	
	/**
	 * 게시글을 수정한다.
	 * @param id 수정할 게시글의 번호
	 * @param boardVO 사용자가 입력한 수정된 게시글의 정보(제목, 이메일, 내용)
	 * @return 
	 */
	@PostMapping("/board/modify/{id}")
	public String doBoardModify(@PathVariable int id, BoardVO boardVO) {
		
		// Command Object에는 전달된 ID가 없으므로
		// PathVariable로 전달된 ID를 셋팅해준다.
		boardVO.setId(id);
		
		boolean isUpdatedSuccess = this.boardService.updateOneBoard(boardVO);
		
		if(isUpdatedSuccess) {
			System.out.println("수정에 성공했습니다!");
		}
		else {
			System.out.println("수정 실패했습니다!");
		}
		
		return "redirect:/board/view?id=" + id; //게시글 내용보기 페이지로 이동시킴
	}
	
	
	/*
	 * GET / POST
	 * 
	 * GET : 데이터 조회할때 사용 (페이지 보여주기, 게시글정보 보여주기)
	 * POST : 데이터 등록할때 사용 (게시글 등록하기)
	 * PUT : 데이터 수정(게시글 수정하기, 좋아요 처리하기, 추천 처리하기)
	 * DELETE : 데이터 삭제(게시글 삭제하기, 댓글 삭제하기)
	 * 
	 * JSP의 경우에 만 : PUT, DELETE지원하지 않음. 오로지 GET, POST만 지원
	 * -- 데이터 조회, 등록, 수정, 삭제 : GET / POST 이용해서 작성
	 * 
	 * FORM으로 데이터를 등록하거나 수정할 경우 - > POST
	 * URL이나 링크등으로 데이터를 조회하거나 삭제할경우 -> GET
	 * 
	 * 
	 */
	
	//삭제 - 이 게시글을 지워라
	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id) {
		
		//삭제 요청
		boolean isDeletedSuccess = this.boardService.deleteOneBoard(id);
		if (isDeletedSuccess) {
			System.out.println("게시글 삭제 성공.");
		}else {
			System.out.println("게시글 삭제 실패.");
		}
		
		//게시글 목록으로 이동
		return "redirect:/board/list";
	}
	
	
	
	
	
}


