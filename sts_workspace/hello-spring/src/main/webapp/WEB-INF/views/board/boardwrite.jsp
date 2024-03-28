<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 작성하기</title>
    <!-- <link rel="stylesheet" href="/css/common.css" /> -->
    <jsp:include page="../commonheader.jsp"></jsp:include>

    <style type="text/css">
      /*div인데 클래스가 grid인것*/
      div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: 28px 28px 28px 320px 1fr;
        row-gap: 10px;
      }
    </style>
    <!-- <script type="text/javascript" src="/js/lib/jquery-3.7.1.min.js"></script>-->
    <script type="text/javascript" src="/js/boardwrite.js"></script>

    <!-- <script type="text/javascript">
      window.onload = function () {
        var dialog = document.querySelector(".alert-dialog");
        dialog.showModal();
      };
    </script> -->
  </head>
  <body>
    <c:if test="${not empty errorMessage}">
      <dialog class="alert-dialog">
        <h1>${errorMessage}</h1>
      </dialog>
    </c:if>

    <jsp:include page="../member/membermenu.jsp"></jsp:include>

    <h1>게시글 작성</h1>
    <!-- multipart form data 형식으로 데이터를 보내야만 한다-->
    <form action="/board/write" method="post" enctype="multipart/form-data">
      <div class="grid">
        <label for="subject">제목</label>
        <input
          id="subject"
          type="text"
          name="subject"
          value="${boardVO.subject}"
        />

        <label for="email">이메일</label>
        <input id="email" type="email" name="email" value="${boardVO.email}" />

        <label for="file">첨부파일</label>
        <input type="file" name="file" id="file" />

        <label for="content">내용</label>
        <textarea id="content" name="content" style="height: 300px">
${boardVO.content}</textarea
        >

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="저장" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>
