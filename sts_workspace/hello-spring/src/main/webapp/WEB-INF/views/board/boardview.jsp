<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 내용</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>

    <link rel="stylesheet" href="/css/common.css" />

    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: repeat(7, 28px) 320px 1fr 1fr;
        row-gap: 10px;
      }
    </style>

    <script type="text/javascript" src="/js/boardview.js"></script>
  </head>
  <body>
    <jsp:include page="../member/membermenu.jsp"></jsp:include>
    <h1>게시글 작성</h1>
    <div class="grid" data-id="${boardVO.id}">
      <label for="subject">제목</label>
      <div>${boardVO.subject}</div>

      <label for="email">작성자이름</label>
      <div>${boardVO.memberVO.name}</div>

      <label for="viewCnt">조회수</label>
      <div>${boardVO.viewCnt}</div>

      <label for="originFileName">첨부파일</label>
      <div>
        <a href="/board/file/download/${boardVO.id}">
          ${boardVO.originFileName}
        </a>
      </div>

      <label for="crtDt">등록일</label>
      <div>${boardVO.crtDt}</div>

      <label for="mdfyDt">수정일</label>
      <div>${boardVO.mdfyDt}</div>

      <label for="content">내용</label>
      <div>${boardVO.content}</div>

      <div class="replies">
        <div class="reply-items"></div>
        <div class="write-reply">
          <textarea id="txt-reply"></textarea>
          <button id="btn-save-reply">등록</button>
          <button id="btn-save-reply">취소</button>
        </div>
      </div>

      <div class="btn-group">
        <c:if test="${sessionScope._LOGIN_USER_.email eq boardVO.email}">
          <div class="right-align">
            <a href="/board/modify/${boardVO.id}">수정</a>
            <a href="/board/delete/${boardVO.id}">삭제</a>
          </div>
        </c:if>
      </div>

      <!--JSP 페이지에서 로그인한 사용자와 게시글의 작성자가 동일한 경우에만 수정과 삭제 링크를 표시-->
      <c:if test="${sessionScope._LOGIN_USER_.email eq boardVO.email}">
        <!--세션에 저장된 현재 로그인한 사용자의 이메일 == 해당 게시글의 작성자의 이메일 이라면, -->
        <div class="btn-group">
          <div class="right-align">
            <a href="/board/modify/${boardVO.id}">수정</a>
            <!--
            javascript:void(0);
            주로 anchor태그의 href에 작성하는 코드.
            링크를 클릭했을때, javascript를 이용해서 처리할 경우 위처럼 작성을 한다.
            javascript:void(0); 이 코드는 anchor태그의 링크이동을 무시한다.
          -->
            <a class="delete-board" href="javascript:void(0);">삭제</a>
            <!--이 링크를 클릭했을 때 JavaScript를 이용한 이벤트만 처리하고 실제로는 어떤 페이지로 이동하지 않도록 -->
          </div>
        </div>
      </c:if>
    </div>
  </body>
</html>
