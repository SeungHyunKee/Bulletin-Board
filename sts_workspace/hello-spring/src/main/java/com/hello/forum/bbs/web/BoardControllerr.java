package com.hello.forum.bbs.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;
import com.hello.forum.bbs.vo.SearchBoardVO;
import com.hello.forum.beans.FileHandler;
import com.hello.forum.exceptions.MakeXlsxFileException;
import com.hello.forum.exceptions.PageNotFoundException;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.AjaxResponse;
import com.hello.forum.utils.ValidationUtils;

import io.github.seccoding.excel.option.WriteOption;
import io.github.seccoding.excel.write.ExcelWrite;
import jakarta.servlet.http.HttpSession;

//import jakarta.validation.Valid;

@Controller
public class BoardControllerr {

	private Logger logger = LoggerFactory.getLogger(BoardControllerr.class);
	/*
	 * 파일다운로드를 위해, fileHandler을 이용하기위해 멤버변수 선언
	 */
	@Autowired
	private FileHandler fileHandler;
	
	/*
	 * Bean Container 에서 BoardService타입객체를 찾아
	 * 아래 멤버변수에게 할당한다(DI)
	 */
	@Autowired
	private BoardService boardService;
	
//	@GetMapping("/board/list")
//	public String viewBoardListPage(Model model) {
//		
//		// 1. 게시글의 건수와 게시글의 목록을 조회해서
//		BoardListVO boardListVO = this.boardService.getAllBoard();
//		
//		// 2. /WEB-INF/vies/board/boardlist.jsp페이지에게 게시글의 건수와 게시글의 목록을 전달하고
//		model.addAttribute("boardList", boardListVO);
//		
//		// 3.화면을 보여준다
//		return "board/boardlist";
//	}
	
	@GetMapping("/board/search")
	public String viewBoardListPage(Model model, SearchBoardVO searchBoardVO) {
		
		BoardListVO boardListVO = this.boardService.searchAllBoard(searchBoardVO);
		model.addAttribute("boardList", boardListVO);
		model.addAttribute("searchBoardVO", searchBoardVO);
		return "board/boardlist";
	}
	
//	/**
//	 * 게시글 작성 페이지를 보여주는 URL
//	 * @return
//	 */
//	@GetMapping("/board/write") //브라우저에서 링크를 클릭, 브라우저 url을 직접 입력
//	public String viewBoardWritePage(){
//		return "board/boardwrite";
//	}
	
	@GetMapping("/board/write")
	public String viewBoardWritePage(HttpSession session) {
		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_");
		if(memberVO == null) {
			return "redirect:/member/login";
		}
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
			
			
//			@Valid   //@Valid : @NotEmpty, @Email, @Size, @Min, @Max 이런것들을 검사하도록 지시 
			BoardVO boardVO, //사용자가 데이터를 안보내주면 여기엔 데이터가 없는것
//			BindingResult bindingResult, 
			//Bindingresult : @Valid에 의해 실행된 파라미터 검사(NotEmpty, Email, Size, Min, Max)의 결과
			@RequestParam MultipartFile file,
			@SessionAttribute("_LOGIN_USER_") MemberVO memberVO, Model model) {
			
			{
		
		/*
		* Servlet Like
		- HttpServletRequest 를 이용
		- Interceptor에서 이용
		- Filter 에서 이용
		*/
		logger.info("글등록 처리를 해야됩니다.");
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

		//검사내용 확인
//		if(bindingResult.hasErrors()) {
//			model.addAttribute("boardVO", boardVO);
//			return "board/boardwrite";
//		}
		
		
		//수동검사 시작 (체크해야하는 파라미터 개수만큼 적어줌)
		boolean isNotEmptySubject = ValidationUtils.notEmpty(boardVO.getSubject());
		
		boolean isNotEmptyContent = ValidationUtils.notEmpty(boardVO.getContent());
		
		if(! isNotEmptySubject) {
			//제목을 입력하지 않았다면
			model.addAttribute("errorMessage", "제목은 필수입력값 입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardwrite";
		}
		
		
		if(! isNotEmptyContent) {
			//내용을 입력하지 않았다면
			model.addAttribute("errorMessage", "내용은 필수입력값 입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardwrite";
		}
		boardVO.setEmail(memberVO.getEmail()); //세션에 있는 이메일을 글등록할때 넣어라(굳이 이메일 추가입력 안해도 됨)
		
		
		
		
		boolean isCreateSuccess = this.boardService.createNewBoard(boardVO, file);
		if (isCreateSuccess) {
			logger.info("글 등록 성공!");
		}
		else {
			logger.info("글 등록 실패!");
		}
		
		// board/boardlist페이지를 보여주는 URL로 이동처리.
		// redirect:/board/search => 스프링은 브라우저에게 /board/search로 이동하라는 명령을 전송
		// 명령을 받은 브라우저는 /board/search로 url을 이동시킨다
		// 그 이후에 /board/search로 브라우저가 요청을 하게되면
		// 스프링 컨트롤러에서 /board/search URL에 알맞은 처리를 진행한다
		return "redirect:/board/search";}
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
	public String viewBoardModifyPade( @PathVariable int id, Model model,
			@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		//1. 전달받은 id의값으로 게시글을 조회한다
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		if (!memberVO.getEmail().equals(boardVO.getEmail())
				&& memberVO.getAdminYn().equals("N")){
				throw new PageNotFoundException();
		}
		
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
	public String doBoardModify(@PathVariable int id, 
								 BoardVO boardVO, 
								 @RequestParam MultipartFile file,
								 Model model,
								 @SessionAttribute("_LOGIN_USER_")MemberVO memberVO) {
		BoardVO originalBoardVO = this.boardService.getOneBoard(id, false);
		if (!originalBoardVO.getEmail().equals(memberVO.getEmail())
				&& memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}
		
		//수동검사 시작 (체크해야하는 파라미터 개수만큼 적어줌)
		boolean isNotEmptySubject = ValidationUtils.notEmpty(boardVO.getSubject());
		boolean isNotEmptyContent = ValidationUtils.notEmpty(boardVO.getContent());
		
		if(! isNotEmptySubject) {
			//제목을 입력하지 않았다면
			model.addAttribute("errorMessage", "제목을 필수입력값 입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardmodify";
		}
		
		
		if(! isNotEmptyContent) {
			//내용을 입력하지 않았다면
			model.addAttribute("errorMessage", "내용은 필수입력값 입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardmodify";
		}
		
	
		// Command Object에는 전달된 ID가 없으므로
		// PathVariable로 전달된 ID를 셋팅해준다.
		boardVO.setId(id);
//		boardVO.setEmail(memberVO.getEmail());
		
		boolean isUpdatedSuccess = this.boardService.updateOneBoard(boardVO, file);
		
		if(isUpdatedSuccess) {
			logger.info("수정에 성공했습니다!");
		}
		else {
			logger.info("수정 실패했습니다!");
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
	
	@ResponseBody
	@PostMapping("/ajax/board/delete/massive")
	public AjaxResponse doDeleteMassive(@RequestParam("deleteItems[]") List<Integer> deleteItems,
			@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		
		if (memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}
		
		boolean deleteResult = this.boardService.deleteManyBoard(deleteItems);
		
		return new AjaxResponse().append("result", deleteResult);
	}
	
	
	//삭제 - 이 게시글을 지워라
	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id,
								@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		
		BoardVO originalBoardVO = this.boardService.getOneBoard(id, false);
		if (!originalBoardVO.getEmail().equals(memberVO.getEmail()) 
				&& memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}
		
		//삭제 요청
		boolean isDeletedSuccess = this.boardService.deleteOneBoard(id);
		if (isDeletedSuccess) {
			logger.info("게시글 삭제 성공.");
		}else {
			logger.info("게시글 삭제 실패.");
		}
		
		//게시글 목록으로 이동
		return "redirect:/board/search";
		
	}
	
	@GetMapping("/board/file/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int id){
		
		//파일 다운로드를 위해서 id 값으로 게시글을 조회한다
		BoardVO boardVO = this.boardService.getOneBoard(id, false); //조회수늘어나면안되므로 flase
		
		//만약, 게시글이 존재하지 않다면 "잘못된 접근입니다" 라는 에러를 사용자에게 보여준다
		if (boardVO == null) {
			throw new PageNotFoundException();
		}
		
		//첨부된 파일이 없을경우에도 "잘못된 접근입니다" 라는 에러를 사용자에게 보여준다
		if ( boardVO.getFileName()==null || boardVO.getFileName().length()==0) {
			throw new PageNotFoundException();
		}
		
		//첨부된파일이 있을경우엔 파일을 사용자에게 보내준다(Download)
		return this.fileHandler.download(boardVO.getOriginFileName(), boardVO.getFileName());
	}
	
	@GetMapping("/board/excel/download2")
	public ResponseEntity<Resource> downloadExcelFile2(){
		BoardListVO boardListVO = boardService.getAllBoard(); //게시글 조회
		
		WriteOption<BoardVO>writeOption = new WriteOption<>();
		writeOption.setFileName("게시글 목록.xlsx");
		writeOption.setFilePath("C:\\uploadFiles");
		writeOption.setContents(boardListVO.getBoardList());
		
		File excelFile = ExcelWrite.write(writeOption);
		
		return this.fileHandler.download("게시글_목록.xlsx", excelFile.getName());
		
	}
	
	
	//excel 다운로드
	@GetMapping("/board/excel/download")
	public ResponseEntity<Resource> downloadExcelFile(){
		
		//(엑셀 파일을 쓸) 모든게시글 조회
		BoardListVO boardListVO = boardService.getAllBoard();
		//XLSX문서 만들기
		Workbook workbook = new SXSSFWorkbook(-1);
		//엑셀시트 만들기
		Sheet sheet = workbook.createSheet("게시글 목록");
		// 행 만들기
		Row row = sheet.createRow(0);
		// 타이틀 만들기
		Cell cell = row.createCell(0);
		cell.setCellValue("번호");
		
		cell = row.createCell(1);
		cell.setCellValue("제목");
		
		cell = row.createCell(2);
		cell.setCellValue("첨부파일명");
		
		cell = row.createCell(3);
		cell.setCellValue("작성자이메일");
		
		cell = row.createCell(4);
		cell.setCellValue("조회수");
		
		cell = row.createCell(5);
		cell.setCellValue("등록일");
		
		cell = row.createCell(6);
		cell.setCellValue("수정일");
		
		//데이터 행 만들고 쓰기
		List<BoardVO> boardList = boardListVO.getBoardList();
		int rowIndex = 1;
		for(BoardVO boardVO : boardList) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue(""+boardVO.getId());
			
			cell = row.createCell(1);
			cell.setCellValue(""+boardVO.getSubject());
			
			cell = row.createCell(2);
			cell.setCellValue(""+boardVO.getOriginFileName());
			
			cell = row.createCell(3);
			cell.setCellValue(""+boardVO.getEmail());			
			
			cell = row.createCell(4);
			cell.setCellValue(""+boardVO.getViewCnt());
			
			cell = row.createCell(5);
			cell.setCellValue(""+boardVO.getCrtDt());
			
			cell = row.createCell(6);
			cell.setCellValue(""+boardVO.getMdfyDt());
			
			rowIndex += 1;
		}
		
		//엑셀파일 만들기
		File storedFile = fileHandler.getStoredFile("게시글_목록.xlsx");
		OutputStream os = null;
		try {
			os = new FileOutputStream(storedFile);
			workbook.write(os);
		}catch (IOException e) {
			throw new MakeXlsxFileException();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				
			}
			if (os != null) {
				try {
					os.flush();
				}catch (IOException e) {
				}
				try {
					os.close();
				}catch (IOException e) {
				}
			}
		}
		
		return this.fileHandler.download("게시글_목록.xlsx", "게시글_목록.xlsx");
//		return this.fileHandler.download("게시글_목록.xlsx", storedFile.getName());

	}
	
	@ResponseBody // ajax쓸때 붙여야됨(ajax로 요청처리를 전부 할 것이므로)
	@PostMapping("/ajax/board/excel/write")
	public AjaxResponse doExcelUpload(@RequestParam MultipartFile excelFile) {
		
//		boolean isSuccess = this.boardService.createMassiveBoard(excelFile);
		boolean isSuccess = this.boardService.createMassiveBoard2(excelFile);

		
		return new AjaxResponse().append("result", isSuccess).append("next", "/board/search"); 
																		//성공여부와 다음링크를 돌려줌
	}

	
}


