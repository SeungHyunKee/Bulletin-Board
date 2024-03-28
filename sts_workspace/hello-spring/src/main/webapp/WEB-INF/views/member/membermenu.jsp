<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="right-align">
  <ul class="horizontal-list">
    <c:choose>
      <%-- 이게 비어있다면 로그인이 안된 상황 --%> <%-- jsp에서는 세션에
      접근할때 sessionScope으로 접근한다 --%>
      <c:when test="${empty sessionScope._LOGIN_USER_}">
        <li>
          <a href="/member/regist">회원가입</a>
        </li>
        <li>
          <a href="/member/login">로그인</a>
        </li>
      </c:when>
      <%--_LOGIN_USER_ 에 memberVO가 들어있음. memberVO에 들어잇는 name을
      보여달라는것 --%>
      <c:otherwise>
        <li style="margin-right: 1rem">
          ${sessionScope._LOGIN_USER_.name} (${sessionScope._LOGIN_USER_.email})
        </li>
        <li>
          <a href="/member/logout">로그아웃</a>
        </li>
        <li>
          <a class="deleteMe" href="javascript:void(0)">탈퇴</a>
        </li>
      </c:otherwise>
    </c:choose>
  </ul>
</div>
