<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원가입</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>

    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 120px 1fr;
        grid-template-rows: 28px 28px 28px 28px 1fr;
        row-gap: 10px;
      }
      div.grid > div.btn-group {
        display: grid;
        grid-column: 1 / 3;
      }
      div.grid div.right-align {
        text-align: right;
      }
      label {
        padding-left: 10px;
      }
      button,
      input {
        padding: 10px;
      }

      .available{
        background-color: #0f03;
      }

      .unusable{
        background-color: #f003;
      }

      input[type="file"] {
        padding: 0px;
      }

      div.errors {
        background-color: #ff00004a;
        opacity: 0.8;
        padding: 10px;
        color: #333;
      }
      div.errors:last-child {
        margin-bottom: 15px;
      }
    </style>
    <!-- 아래코드로 인해 자바스크립트 사용할 수 있게 됨 -->
    <script type="text/javascript" src="/js/lib/jquery-3.7.1.min.js"></script> 
    <script type="text/javascript">
        $().ready(function() {
          var alertDialog = $(".alert-dialog");

        // //자바스크립트에서는 아래 if문 결과 : 모두 false 이다.
        // if(undefined){ 
        //   //실행되지 않음
        // }
        // if(null){ 
        //   //실행되지 않음
        // }
        // if(0){ 
        //   //실행되지 않음
        // }
        // if(""){ 
        //   //실행되지 않음
        // }
        // if(false){ 
        //   //실행되지 않음
        // }

        console.log(alertDialog);

          if(alertDialog && alertDialog.length > 0){
            alertDialog[0].showModal();
          }

          $("#email").on("keyup", function(){
            //서버에게 사용할 수 있는 이메일인지 확인받는다
            $.get("/ajax/member/regist/available", {"email":$(this).val()}, function(response){
              var available = response.available;
              if(available){
                $("#email").addClass("available"); //동적으로 클래스가 할당됨
                $("#email").removeClass("unusable");
                $("btn-regist").removeAttr("disabled"); 
              } else{
                $("#email").addClass("unusable");
                $("#email").removeClass("available");
                $("btn-regist").attr("disabled", "disabled"); //사용불가능한 아이디 적었을때 등록버튼 누를수없음
              }
            });
          });
        });

      window.onload = function () {
        var dialog = document.querySelector(".alert-dialog");
        dialog.showModal();
      };
    </script>
  </head>
  <body>
    <c:if test="${not empty errorMessage}">
      <dialog class="alert-dialog">
        <h1>${errorMessage}</h1>
      </dialog>
    </c:if>

    <h1>회원가입</h1>
    <form method="post">
      <div class="grid">
        <label for="email">이메일</label>
        <input id="email" type="email" name="email" />

        <label for="name">이름</label>
        <input id="name" type="text" name="name" />

        <label for="password">비밀번호</label>
        <input
          id="password"
          type="password"
          name="password"
        />

        <label for="confirmPassword">비밀번호 확인</label>
        <input
          id="confirmPassword"
          type="password"
          name="confirmPassword"/>
      
          <div class="btn-group">
            <div class="right-align">
              <input id="btn-regist" type="submit" value="등록" />
          </div>
        </div>
    </form>
</body>
</html>
