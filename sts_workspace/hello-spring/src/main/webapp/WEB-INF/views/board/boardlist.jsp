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
        grid-template-rows: 28px 1fr 28px 28px;
        row-gap: 10px;
      }
    </style>
    <script type="text/javascript" src="/js/boardlist.js"></script>
  </head>
  <body>
    <jsp:include page="../layout/layout.jsp" />
    <div class="grid">

      <div class="right-align">
        총 ${boardList.boardCnt} 건의 게시글이 검색되었습니다.
      </div>
      <table class="table">
        <colgroup>
          <c:if test="${sessionScope._LOGIN_USER_.adminYn eq 'Y'}">
            <col width="40px" />
          </c:if>
          <col width="80px" />
          <col width="*" />
          <col width="150px" />
          <col width="80px" />
          <col width="150px" />
          <col width="150px" />
        </colgroup>
        <thead>
          <tr>
            <c:if test="${sessionScope._LOGIN_USER_.adminYn eq 'Y'}">
              <th>
                <input type="checkbox" 
                      id="checked-all" 
                      data-target-class="target-board-id" />
              </th>
            </c:if>
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
                  <c:if test="${sessionScope._LOGIN_USER_.adminYn eq 'Y'}">
                    <td>
                      <input type="checkbox" 
                            class="target-board-id" 
                            name="targetBoardId" 
                            value="${board.id}" />
                    </td>
                  </c:if>
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
        <form id="search-form">
          <!-- value=0 은 아무것도 안치고 엔터눌렀을때 에러나는것을 방지하는 하는 역할-->
          <input type="hidden" id="page-no" name="pageNo" value="0"/>
          <select id="list-size" name="listSize">
            <option value="10" ${searchBoardVO.listSize eq 10 ? 'selected' : ''}>10개</option>
            <option value="20" ${searchBoardVO.listSize eq 20 ? 'selected' : ''}>20개</option>
            <option value="30" ${searchBoardVO.listSize eq 30 ? 'selected' : ''}>30개</option>
            <option value="50" ${searchBoardVO.listSize eq 50 ? 'selected' : ''}>50개</option>
            <option value="100" ${searchBoardVO.listSize eq 100 ? 'selected' : ''}>100개</option>
          </select>

          <!--검색하는 타입을 만들어줌-->
          <select id="search-type" name="searchType">
            <option value="title" ${searchBoardVO.searchType eq 'title' ? 'selected' : ''}>제목</option>
            <option value="content" ${searchBoardVO.searchType eq 'content' ? 'selected' : ''}>내용</option>
            <option value="title_content" ${searchBoardVO.searchType eq 'title_content' ? 'selected' : ''}>제목 + 내용</option>
            <option value="email" ${searchBoardVO.searchType eq 'email' ? 'selected' : ''}>작성자</option>
          </select>

          <input type="text" name="searchKeyword" value="${searchBoardVO.searchKeyword}"/>
          <button type="button" id="search-btn">검색</button>
          <button type="button" id="cancel-search-btn">초기화</button>


          <ul class="page-nav">
            <c:if test="${searchBoardVO.hasPrevGroup}">
              <!-- '처음'을 클릭하면, javascript에 있는 search함수에 0을 파라미터로 전달해서 form을 전송시킴 -->
              <li><a href="javascript:search(0);">처음</a></li>
              <li>
                <a
                  href="javascript:search(${searchBoardVO.prevGroupStartPageNo});"
                  >이전</a
                >
              </li>
            </c:if>

            <!--page번호를 반복하여 노출한다.-->
            <c:forEach
              begin="${searchBoardVO.groupStartPageNo}"
              end="${searchBoardVO.groupEndPageNo}"
              step="1"
              var="p"
            >
              <!--페이지번호(pageNo) 는 항상 0부터 시작 -->
              <li class="${searchBoardVO.pageNo eq p ? 'active' : ''}">
                <!--p와 누른페이지번호가 같다면 active줘라. 노출되는것은 1부터 시작하므로 +1 해줌-->
                <!-- p를 넣어서 search함수를 호출한다 -->
                <a href="javascript:search(${p});">${p+1}</a>
              </li>
            </c:forEach>

            <c:if test="${searchBoardVO.hasNextGroup}">
              <li>
                <a
                  href="javascript:search(${searchBoardVO.nextGroupStartPageNo});"
                  >다음</a
                >
              </li>
              <li>
                <a href="javascript:search(${searchBoardVO.pageCount - 1});"
                  >마지막</a
                >
              </li>
            </c:if>
          </ul>
        </form>
      </div>

      <!-- Paginator 끝-->

      <c:if test="${not empty sessionScope._LOGIN_USER_}">
        <div class="right-align">
          <c:if test="${sessionScope._LOGIN_USER_.adminYn eq 'Y'}">
            <a href="/board/excel/download2">엑셀 다운로드</a>
          </c:if>
          <a href="/board/write">게시글 등록</a>
          <c:if test="${sessionScope._LOGIN_USER_.adminYn eq 'Y'}">
            <a id="deleteMassiveBoard" href="javascript:void(0);">일괄삭제</a>
            <a id="uploadExcelfile" href="javascript:void(0);"
              >게시글 일괄 등록</a>
            <!--이(script)게 클릭되면 아래줄이 실행되도록 함-->
            <input type="file" id="excelfile" style="display: none" />
          </c:if>
        </div>
      </c:if>
      </div>
      <jsp:include page="../layout/layout_close.jsp" />
  </body>
</html>
