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
    <!-- <script type="text/javascript">
      //1.페이지 렌더링이 모두 끝났을 때
      window.onload = function () {
        // 삭제 링크를 클릭하면
        var deleteAnchor = document.querySelector(".delete-board");
        deleteAnchor.addEventListener("click", function () {
          // 사용자에게 진짜 삭제할 것인지? 물어보고
          var chooseValue = confirm(
            "이 게시글을 정말 삭제하시겠습니까?\n삭제작업은 복구할수 없습니다."
          );
          //chooseValue가 true라면 "확인" 클릭
          //chooseValue가 false라면 '취소' 클릭
          // 삭제를 하려한다면 : 삭제처리
          // 그렇지않다면 : 아무일도 하지 않는다
          if (chooseValue) {
            location.href = "/board/delete/${boardVO.id}";
          }
        });
      };
    </script> -->
    <!-- <script type="text/javascript" src="/js/lib/jquery-3.7.1.min.js"></script>-->
    <!-- <script type="text/javascript" src="/js/boardview.js"></script> -->
    <!-- <script type="text/javascript" src="/js/lib/jquery-3.7.0.js"></script>
    <script type="text/javascript">
                $().ready(function() {
                  var modifyReply = function(event) {
                  var reply = $(event.currentTarget).closest(".reply");
                  var replyId = reply.data("reply-id");

                  var content = reply.find(".content").text();
                  $("#txt-reply").val(content);
                  $("#txt-reply").focus();

                  $("#txt-reply").data("mode", "modify");
                  $("#txt-reply").data("target", replyId);
                }

              var deleteReply = function(event) {
              var reply = $(event.currentTarget).closest(".reply");
              var replyId = reply.data("reply-id");

              $("#txt-reply").removeData("mode");
              $("#txt-reply").removeData("target");

              if (confirm("댓글을 삭제하시겠습니까?")) {
              $.get(`/board/reply/delete/\${replyId}`, function(response) {
              var result = response.result;
              if (result) {
                loadReplies();
                ("#txt-reply").val("");
                }
            });
            }
          }
          var reReply = function(event) {
          var reply = $(event.currentTarget).closest(".reply");
          var replyId = reply.data("reply-id");

          $("#txt-reply").data("mode", "re-reply");
          $("#txt-reply").data("target", replyId);
          $("#txt-reply").focus();
        }

            var recommendReply = function(event) {
          var reply = $(event.currentTarget).closest(".reply");
          var replyId = reply.data("reply-id");
        $("#txt-reply").removeData("mode");
        $("#txt-reply").removeData("target");

          $.get(`/board/reply/recommend/\${replyId}`, function(response) {
            var result = response.result;
            console.log(result)
          if (result) {
            loadReplies();
            $("#txt-reply").val("");
            }
        });
        }

        // 댓글 조회하기.
        var loadReplies = function() {
            $(".reply-items").html("");
          $.get("/board/reply/${boardVO.id}", function(response) {
            var replies = response.replies;
            for (var i = 0; i < replies.length; i++) {
              var reply = replies[i];
              var replyTemplate =
        div class="reply"  ???



      }
                  }
                }
              } 
    </script>-->
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

      <c:if test="${sessionScope._LOGIN_USER_.email eq boardVO.email}">
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
          </div>
        </div>
      </c:if>
    </div>
  </body>
</html>
