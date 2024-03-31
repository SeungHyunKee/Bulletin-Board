<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <jsp:include page="../commonheader.jsp"></jsp:include>

    <title>로그인</title>
    <link rel="stylesheet" href="/css/common.css" />
    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 120px 1fr;
        grid-template-rows: 28px 28px 1fr;
        row-gap: 10px;
      }

      .error {
        grid-column: 1 / -1;
        color: #f00;
        padding-left: 1rem;
        margin: 0;
      }
    </style>
    <script type="text/javascript" src="/js/lib/jquery-3.7.1.min.js"></script>
    <script type="text/javascript" src="/js/memberregist.js"></script>
  </head>
  <body>
    <h1>로그인</h1>
    <form id="loginForm">
      <input type="hidden" name="next" id="next" value="${nextUrl}" />
      <div class="grid">
        <label for="email">이메일</label>
        <input type="email" name="email" id="email" />

        <label for="password">비밀번호</label>
        <input
          type="password"
          name="password"
          autocomplete="new-password"
          id="password"
        />

        <div class="btn-group">
          <div class="right-align">
            <button id="btn-login" type="button">로그인</button>
          </div>
        </div>
      </div>
    </form>
    <!-- <div>아이디 또는 비밀번호가 잘못되었습니다.</div> -->
  </body>
</html>
