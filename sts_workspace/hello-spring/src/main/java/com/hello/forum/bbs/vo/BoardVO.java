package com.hello.forum.bbs.vo;

import com.hello.forum.member.vo.MemberVO;

import io.github.seccoding.excel.annotations.ExcelSheet;
import io.github.seccoding.excel.annotations.Field;
import io.github.seccoding.excel.annotations.Format;
import io.github.seccoding.excel.annotations.Title;

//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;

@ExcelSheet(value = "Sheet1", useTitle = true, startRow = 2)
public class BoardVO {
	
	
	//엑셀 쓰기
	@Title("번호")
	@Format(alignment = Format.LEFT)
	private int id;
	
	//엑셀 읽기
	@Field("B")
//	@NotEmpty(message= "제목은 필수입력값 입니다.")
	@Title("제목")
	@Format(alignment = Format.LEFT)
	private String subject;
	
//	@NotEmpty(message= "내용은 필수입력값 입니다.") //필수입력값 설정하는 방법 : 	@NotEmpty
	@Field("C")
	private String content;
	
//	@NotEmpty(message= "이메일은 필수입력값 입니다.") //필수 입력값 체크
//	@Email(message="올바른 형식으로 입력하세요.") //입력값이 이메일 형태인지 검사
	@Field("A")
	@Title("작성자 이메일")
	@Format(alignment = Format.LEFT)
	private String email;
	@Title("조회수")
	@Format(alignment = Format.LEFT)
	private int viewCnt;
	@Title(value = "등록일", date = true)
	@Format(alignment = Format.LEFT, dataFormat="yyyy-MM-dd")
	private String crtDt;
	@Title(value = "수정일", date = true)
	@Format(alignment = Format.LEFT, dataFormat="yyyy-MM-dd")
	private String mdfyDt;
	/**
	 * 서버에 저장된 파일의 이름 : fileName (난독화처리된 파일명)
	 */
	private String fileName;
	/**
	 * 사용자가 업로드한 파일의 실제이름 : originFileName
	 */
	@Title("첨부파일명")
	@Format(alignment = Format.LEFT)
	private String originFileName;
	
	private String delYn;
	
	private MemberVO memberVO;
	
	
	public MemberVO getMemberVO() {
		return memberVO;
	}
	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	public String getCrtDt() {
		return crtDt;
	}
	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}
	public String getMdfyDt() {
		return mdfyDt;
	}
	public void setMdfyDt(String mdfyDt) {
		this.mdfyDt = mdfyDt;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOriginFileName() {
		return originFileName;
	}
	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
		
}