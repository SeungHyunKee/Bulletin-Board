<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 목록</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <!-- <link rel="stylesheet" href="/css/common.css" /> -->
    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 1fr;
        grid-template-rows: 28px 28px 1fr 28px 28px;
        row-gap: 10px;
      }
    </style>
    <script type="text/javascript" src="/js/boardlist.js"></script>
  </head>
  <body>
    <div class="grid">
      <jsp:include page="../member/membermenu.jsp"></jsp:include>

      <div class="right-align">
        총 ${boardList.boardCnt} 건의 게시글이 검색되었습니다.
      </div>
      <table class="table">
        <colgroup>
          <col width="80px" />
          <col width="*" />
          <col width="150px" />
          <col width="80px" />
          <col width="150px" />
          <col width="150px" />
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>이메일</th>
            <th>조회수</th>
            <th>등록일</th>
            <th>수정일</th>
          </tr>
        </thead>
        <tbody>
          <!--
	        boardlist의 내용이 존재한다면(1개이상 있다면), 
	        	내용을 반복해서 보여주고
	        boardList의 내용이 존재하지 않는다면
	         	"등록된 게시글이 없습니다. 첫번째 글의 주인공이 되어보세요!를 보여준다. 
	        jstl > choose when otherwise 사용 => java의 if else if else 와 같은문법
	         -->
          <!-- 조건식의 시작을 알림 -->
          <c:choose>
            <%--boardlist의 내용이 존재한다면(1개이상 있다면)--%>
            <c:when test="${not empty boardList.boardList}">
              <%--내용을 반복해서 보여주고 --%>
              <c:forEach items="${boardList.boardList}" var="board">
                <tr>
                  <td class="center-align">${board.id}</td>
                  <td class="left-align">
                    <a class="ellipsis" href="/board/view?id=${board.id}"
                      >${board.subject}</a
                    >

                    <c:if test="${not empty board.originFileName}">
                      <a href="/board/file/download/${board.id}"
                        >[첨부파일 다운로드 click]</a
                      >
                    </c:if>
                  </td>
                  <td class="center-align">${board.memberVO.name}</td>
                  <!--작성자의 이름이 보임-->
                  <td class="center-align">${board.viewCnt}</td>
                  <td class="center-align">${board.crtDt}</td>
                  <td class="center-align">${board.mdfyDt}</td>
                </tr>
              </c:forEach>
            </c:when>
            <%-- boardList의 내용이 존재하지 않는다면 --%>
            <c:otherwise>
              <tr>
                <td colspan="6">
                  <a href="/board/write">
                    등록된 게시글이 없습니다. 첫번째 글의 주인공이 되어보세요!
                  </a>
                </td>
              </tr>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>

      <!-- Paginator 시작-->
      <div>
        <div>
          <ul class="page-nav">
            <!--page번호를 반복하여 노출한다.-->
            <c:forEach
              begin="1"
              end="${searchBoardVO.pageCount}"
              step="1"
              var="p"
            >
              <!--페이지번호(pageNo) 는 항상 0부터 시작함-->
              <li class="${searchBoardVO.pageNo eq p-1 ? 'active' : ''}">
                <!--p와 누른페이지번호가 같다면 active줘라-->
                <a href="/board/search?pageNo=${p-1}&listSize=10">${p}</a>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>

      <!-- Paginator 끝-->

      <c:if test="${not empty sessionScope._LOGIN_USER_}">
        <div class="right-align">
          <a href="/board/excel/download2">엑셀 다운로드</a>
          <a href="/board/write">게시글 등록</a>
          <a id="uploadExcelfile" href="javascript:void(0);"
            >게시글 일괄 등록</a
          >
          <!--이(script)게 클릭되면 아래줄이 실행되도록 함-->
          <input type="file" id="excelfile" style="display: none" />
        </div>
      </c:if>
    </div>
  </body>
</html>
