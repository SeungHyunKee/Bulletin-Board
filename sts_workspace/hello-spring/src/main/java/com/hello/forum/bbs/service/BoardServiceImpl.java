package com.hello.forum.bbs.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.bbs.dao.BoardDao;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;
import com.hello.forum.beans.FileHandler;
import com.hello.forum.beans.FileHandler.StoredFile;
import com.hello.forum.exceptions.PageNotFoundException;

import io.github.seccoding.excel.option.ReadOption;
import io.github.seccoding.excel.read.ExcelRead;

/*
 * @Service : @Controller와 @Repository를 연결하는 역할을 함
 * 	- 역할 : 주로 Transaction처리 담당
 * 	- = 업무 로직을 담당
 * 업무로직 이란?
 * - 토스에서 통장을 관리하기 위해서는
 * 		  이체(내 통장에서 출금해서 다른통장으로 입금한다)
 * 			- 1. 내 통장에서 출금한다
 *               --> 출금도중 원인을 알수없는 예외가 발생 ?!!!
 *                --> 예외가 발생하면 application이 종료됨
 *                 --> 내 통장에서 출금한 돈은 어딘가로 사라지고 없다ㅠ
 *            -> 이런 현상을 방지하기위해: 업무로직 진행 중 예외 발생할경우 Rollback처리
 *            -> 업무로직이 정상적으로 종료가되었다면 commit 수행 ==> 이게바로 'Transaction'
 * 			- 2. 출금한 돈을 다른통장으로 입금한다.
 * 		  출금(통장에서 돈을 뺌)
 *        입금(통장으로 돈을 넣는다) 
 *        
 * @Controller        
 * @Service
 * @Repository
 * 위 3개는 모두 Spring이 객체로 생성해서 Bean Container에 보관하는 역할
 */


@Service
public class BoardServiceImpl implements BoardService {

	
	private Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	/**
	 * 멤버변수위에 @Autowired를 작성하면
	 * BeanContainer에서 멤버변수의 타입과 일치하는 객체를 찾아서
	 * 멤버변수에게 자동으로 할당해준다(Spring)
	 */
	@Autowired //멤버변수를 이용한 방법
	private BoardDao boardDao;
	
	@Autowired
	private FileHandler fileHandler;
	
	@Override
	public BoardListVO getAllBoard() {
		//BoardDaoImplement의 getBoardAllCount를 이용해서 게시글의 건수를 알고싶고,
		int boardCount = this.boardDao.getBoardAllCount();
		/// BoardDaoImpl의 getAllBoard를 이용해서 게시글의 목록을 알고싶다
		List<BoardVO> boardList = this.boardDao.getAllBoard();
		
		BoardListVO boardListVO = new BoardListVO();
		boardListVO.setBoardCnt(boardCount);
		boardListVO.setBoardList(boardList);
		
		return boardListVO;
	}

	@Transactional
	@Override
	public boolean createNewBoard(BoardVO boardVO, MultipartFile file) {
		
		//사용자가 파일을 업로드 했다면
		if (file != null && ! file.isEmpty()) {
			StoredFile storedFile = fileHandler.storeFile(file);
			
			//업로드한 파일을 서버에 정상적으로 업로드 한 경우
			if (storedFile != null) {
				//난독화 처리된 파일의 이름
				boardVO.setFileName(storedFile.getRealFileName());
				//사용자가 업로드한 파일의 이름
				boardVO.setOriginFileName(storedFile.getFileName());
			}
		}
		
		int insertedCount = this.boardDao.insertNewBoard(boardVO);
		
		//NumberFormatException이 발생하면, rollback된다! (String AOP가 처리해 줌)
//		Integer.parseInt("qwertyuiop"); // <-- 강제로 예외 발생시키기
		
		return insertedCount > 0;
	}

	@Transactional
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		//1.게시글 정보 조회하기
		BoardVO boardVO = this.boardDao.selectOneBoard(id);
		
		//게시글을 조회한 결과가 null이라면, '잘못된 접근입니다' 예외를 발생시킨다
		if(boardVO == null) {
			throw new PageNotFoundException();
		}
		
		if(isIncrease) { //isIncrease가 true라면 조회수 증가시켜라
			//2.게시글의 조회수를 1증가시키기
			this.boardDao.increaseViewCount(id);
//			if (updatedCount == 0) {
//				// 업데이트 영향을 받은 row가 단 한건도 없다면
//				// 사용자가 잘못 요청을 했거나
//				// 부정적인 방법으로 시스템을 이용하는 중으로 판단
//				throw new IllegalArgumentException("잘못된 접근입니다.");
//			}
		}
		return boardVO;
	}

	@Transactional
	@Override
	public boolean updateOneBoard(BoardVO boardVO, MultipartFile file) {
		
		//사용자가 파일을 업로드했는지 확인
		if(file != null && !file.isEmpty()) {
			//기존의 게시글내용 확인 
			// -> 이유 : 사용자가 파일을 업로드한 경우, 기존 업로드되었던 파일을 삭제하기 위함
			// 기존에 첨부된 파일의 존재여부를 확인해야한다
			BoardVO originalBoardVO = this.boardDao.selectOneBoard(boardVO.getId());
			//기존 게시글에 첨부된파일이 있는지 확인
			if(originalBoardVO != null) {
				//기존게시글에 첨부된 파일의 이름을 받아온다
				String storedFileName = originalBoardVO.getFileName();
				//첨부된파일의 이름이 있는지 확인하고,
				//만약 첨부된파일의 이름이 있다면, 이게시글은 파일이 첨부되었던 게시글이다.
				if(storedFileName != null && storedFileName.length() > 0) { //만약 첨부된 파일의 이름이 있다면,
					//첨부된 파일을 삭제한다.
					this.fileHandler.deleteFileByFileName(storedFileName);
				}
			}
			//사용자가 업로드한 파일을 서버에 저장한다.
			StoredFile storedFile = this.fileHandler.storeFile(file);
			boardVO.setFileName(storedFile.getRealFileName());
			boardVO.setOriginFileName(storedFile.getFileName()); 
		}
		
		int updatedCount = this.boardDao.updateOneBoard(boardVO);
		return updatedCount > 0; //updatedCount > 0 이라면 성공이다
	}

	@Transactional
	@Override
	public boolean deleteOneBoard(int id) {
		//기존의 게시글내용 확인 
		BoardVO originalBoardVO = this.boardDao.selectOneBoard(id);
		//기존 게시글에 첨부된파일이 있는지 확인
		if(originalBoardVO != null) {
			//기존게시글에 첨부된 파일의 이름을 받아온다
			String storedFileName = originalBoardVO.getFileName();
			//첨부된파일의 이름이 있는지 확인하고,
			//만약 첨부된파일의 이름이 있다면, 이게시글은 파일이 첨부되었던 게시글이다.
			if(storedFileName != null && storedFileName.length() > 0) { //만약 첨부된 파일의 이름이 있다면,
				//첨부된 파일을 삭제한다.
				this.fileHandler.deleteFileByFileName(storedFileName);
			}
		}
		
		int deletedCount = this.boardDao.deleteOneBoard(id);
		return deletedCount > 0;
	}

	@Transactional
	@Override
	public boolean createMassiveBoard(MultipartFile excelFile) {

		int insertedCount = 0;
		int rowSize = 0;
		
		if (excelFile != null && !excelFile.isEmpty()) {
			StoredFile storedExcel = this.fileHandler.storeFile(excelFile);
		
			if (storedExcel != null) {
				//엑셀파일을 읽는다
				// 1.filesystem에 있는 파일을 java로 읽어와야한다(inputstream)
				InputStream excelFileInputStream = null;
				try {
					excelFileInputStream = new FileInputStream(storedExcel.getRealFilePath());
				} catch(FileNotFoundException e) {
//					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
				// 2.Apache poi를 활용해서 inputStream의 내용을 엑셀문서로 읽어온다
				Workbook excelWorkbook = null;
				if (excelFileInputStream != null) {
					try {
						excelWorkbook = new XSSFWorkbook(excelFileInputStream);
					} catch (IOException e) {
//						e.printStackTrace();
						logger.error(e.getMessage(), e);

					}
				}
				
				
				//엑셀파일의 특정 sheet에 있는 모든 데이터를 찾아 List<BoardVO>로 만들어준다
				List<BoardVO> boardListInExcel = new ArrayList<>();
				if(excelWorkbook != null) {
					//sheet를 추출한다
					Sheet sheet = excelWorkbook.getSheet("Sheet1");
					
					//sheet에서 data가 있는 row만큼 반복을 한다.
					//1.sheet에서 데이터가 몇개의 row로 구성되어있는지 확인한다.
					rowSize = sheet.getPhysicalNumberOfRows();
					//첫번째 row부터 row size만큼 반복
					for(int i = 1; i < rowSize; i++) {
						// 3.i번째 row를 가져온다.
						Row row = sheet.getRow(i);
						
						//4. row에 있는 cell(게시글정보)들을 가져온다.
						String author = row.getCell(0).getStringCellValue();
						String subject = row.getCell(1).getStringCellValue();
						String description = row.getCell(2).getStringCellValue();
						
						BoardVO boardVO = new BoardVO();
						boardVO.setEmail(author);
						boardVO.setSubject(subject);
						boardVO.setContent(description);
						
						boardListInExcel.add(boardVO);
					}
				}
				
				//List<BoardVO>에 있는 내용을 모두 insert 한다.
				for(BoardVO boardVO : boardListInExcel) {
					insertedCount += this.boardDao.insertNewBoard(boardVO);
				}
			}
		}
		
		// 한건이상 insert했고, 엑셀파일의 row개수와 insert한 row개수가 같다면 성공했다고 보는 것.
		return insertedCount > 0 && insertedCount == rowSize - 1;
	}

	

	/**
	 * 더 간단하게 엑셀파일을 읽는 방법
	 */
	@Transactional
	@Override
	public boolean createMassiveBoard2(MultipartFile excelFile) {

		int insertedCount = 0;
		int rowSize = 0;
		
		if (excelFile != null && !excelFile.isEmpty()) {
			StoredFile storedExcel = this.fileHandler.storeFile(excelFile, false); //확장자를 숨기지 않음
		
			if (storedExcel != null) {		
				
				ReadOption readOption = new ReadOption();
				readOption.setFilePath(storedExcel.getRealFilePath());
				
				List<BoardVO> boardListInExcel = new ExcelRead<BoardVO>()
						.readToList(readOption, BoardVO.class);
				rowSize = boardListInExcel.size(); //여기서는 -1을 해줄 필요 없음
				
				//List<BoardVO>에 있는 내용을 모두 insert 한다.
				for(BoardVO boardVO : boardListInExcel) {
					insertedCount += this.boardDao.insertNewBoard(boardVO);
				}
			}
		}
		
		// 한건이상 insert했고, 엑셀파일의 row개수와 insert한 row개수가 같다면 성공했다고 보는 것.
		return insertedCount > 0 && insertedCount == rowSize;
	}

}
