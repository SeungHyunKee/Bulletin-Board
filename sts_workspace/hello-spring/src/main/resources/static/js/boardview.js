$().ready(function () {
  var pageNumber = 0;

  $(document).on("scroll", function () {
    console.log("스크롤함!!!");

    var scrollHeight = $(window).scrollTop();
    // console.log("스크롤 위치" + scrollHeight);
    var documentHeight = $(document).height();
    // console.log("문서내용의 높이" + documentHeight);
    var browserHeight = $(window).height();
    // console.log("브라우저의 높이" + browserHeight);
    var scrollBottomPoint = scrollHeight + browserHeight + 30;
    // console.log("스크롤바 밑 부분의 위치: " + scrollBottomPoint);

    // 스크롤바가 문서의 끝까지 왔다고 판단 될 때, 댓글을 더 불러올지 선택할 수 있도록 함
    var willFetchReply = scrollBottomPoint > documentHeight;
    if (willFetchReply) {
      loadReplies(boardId, pageNumber);
      console.log("댓글을 10개만 더 불러옵니다.");
    }
  });

  $(".delete-board").on("click", function () {
    var chooseValue = confirm(
      "이 게시글을 정말 삭제하시겠습니까? \n삭제작업은 복구할수 없습니다."
    );

    var id = $(this).closest(".grid").data("id");

    if (chooseValue) {
      location.href = "/board/delete/" + id;
    }
  });

  var modifyReply = function (event) {
    // console.log("수정을 클릭함", event);

    //클릭된 요소를 target에 저장
    var target = event.currentTarget;
    // 클릭된 요소를 기준으로 가장가까운 .reply클래스를 가진 부모요소를 찾아 reply에 저장
    // -> 이는 수정할 댓글(reply)를 가리키게 됨
    var reply = $(target).closest(".reply");
    // 선택된댓글의 고유한id를 replyId에 저장 -> 수정할댓글을 서버로 식별하는데 사용
    var replyId = reply.data("reply-id"); //몇번댓글을 클릭했는지 알수있다
    // console.log("replyId", replyId);

    //클릭된 댓글의 내용을 가져옴
    var content = reply.find(".content").text(); // pre태그에 붙어있는, 클릭된댓글내용을 가져와 content에 넣어줌
    //가져온 댓글 내용을 #txt-reply에 채움 -> 사용자가 수정할때 원래댓글내용 볼수있도록함
    $("#txt-reply").val(content);

    $("#txt-reply").focus(); //text area가 활성되어 선택된다
    // txt-reply 데이터-> 수정모드
    $("#txt-reply").data("mode", "modify");
    // txt-reply 데이터에 replyId 저장-> 수정작업을 서버에 보낼때 어떤댓글을 수정하는지 식별
    $("#txt-reply").data("target", replyId);
  };

  var deleteReply = function (event) {
    console.log("삭제를 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    // console.log("replyId", replyId);

    // txt-reply에서 mode라는 데이터 제거 -> 입력란이 수정모드가아닌 상태로 되돌아가는것
    $("#txt-reply").removeData("mode"); //현재 모드를 없애는것. mode에 저장되어있는 데이터변수를 textreply에서 지우는것
    // txt-reply에서 target이라는 데이터 제거 -> 수정대상이 더이상 지정되지 않음을 의미.
    // (수정작업이 완료되면 해당데이터는 필요하지 않으므로)
    $("#txt-reply").removeData("target"); //mode에 저장되어있는 데이터변수를 textreply에서 지우므로 다음 댓글을 나중에 달수있게됨

    if (confirm("댓글을 삭제하시겠습니까?")) {
      $.get("/ajax/board/reply/delete/" + replyId, function (response) {
        var result = response.result;
        if (result) {
          loadReplies(boardId);
          $("txt-reply").val("");
        }
      });
    }
  };
  var reReply = function (event) {
    console.log("답글달기를 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    // console.log("replyId", replyId);

    // #txt-reply의 데이터에 답글작성모드 나타내는 mode 추가
    $("#txt-reply").data("mode", "re-reply");
    // #txt-reply데이터에 대상댓글 아이디 저장 -> 답글작성작업 서버에 보낼때 어떤댓글에대한 답글 작성하는지 식별하는데 사용
    $("#txt-reply").data("target", replyId); //target = 내가클릭한 댓글의 번호 -> 등록할때 그 댓글에 댓글이 달릴것
    // #txt-reply에 포커스 주어 사용자가 바로 답글 작성할 수 있도록
    $("#txt-reply").focus();
  };
  var recommendReply = function (event) {
    console.log("추천하기를 클릭함", event);
    var target = event.currentTarget;
    var reply = $(target).closest(".reply");
    var replyId = reply.data("reply-id");
    // console.log("replyId", replyId);

    // #txt-reply에서 mode 데이터 제거 -> 답글작성모드가 아닌상태로 되돌아가는것
    $("#txt-reply").removeData("mode");
    //#txt-replydptj target데이터 제거 -> 답글작성대상이 더이상 지정되지 않음을 의미
    $("#txt-reply").removeData("target");

    $.get("/ajax/board/reply/recommend/" + replyId, function (response) {
      var result = response.data.result;
      console.log(result);
      if (result) {
        loadReplies(boardId);
        $("txt-reply").val("");
      }
    });
  };

  var loadReplies = function (boardId, pageNo) {
    var isNotUndefinedPageNo = pageNo !== undefined; // pageNo가 전달되었는지 아닌지 판단(undefined라면 파라미터인 pageNo안보내줌)
    var params = { pageNo: -1 }; // -1일때는 값을 전부 다 불러옴, 전달되는 pageNo의 값을 보고 pagination을 할지말지 결정하는것
    if (isNotUndefinedPageNo) {
      params.pageNo = pageNo;
    }

    $(".reply-items").html("");

    $.get("/ajax/board/reply/" + boardId, params, function (response) {
      if (!isNotUndefinedPageNo) {
        // $(".reply-items").html(""); //댓글더불러올때 삭제되면 안되므로
        pageNumber = response.data.paginate.pageCount - 1;
      }
      var count = response.data.count;
      var replies = response.data.replies;

      if (isNotUndefinedPageNo && count == response.data.paginate.listSize) {
        pageNumber++;
      }

      for (var i in replies) {
        var reply = replies[i];

        /***********************이미 불러온 댓글 수정*************************/
        // 이미 불러온 댓글인지 확인
        var appendedReply = $(".reply[data-reply-id=" + reply.replyId + "]");
        var isAppendedReply = appendedReply.length > 0;
        // 이미 불러온 댓글이며, 삭제가 안된 댓글일 경우
        if (isAppendedReply && reply.delYn === "N") {
          appendedReply.find(".content").text(reply.content);
          appendedReply
            .find(".recommend-count")
            .text("추천수: " + reply.recommendCnt);
          var modifyDate = appendedReply.find(".mdfydt");
          if (modifyDate) {
            modifyDate.text("(수정: " + reply.mdfyDt + ")");
          } else {
            var mdfyDtDom = $("<span></span>");
            mdfyDtDom.addClass("mdfydt");
            mdfyDtDom.text("(수정: " + reply.mdfyDt + ")");
            appendedReply.find(".datetime").append(mdfyDtDom);
          }
          continue;
        }
        // 이미 불러온 댓글인데, 삭제가 된 댓글일 경우
        else if (isAppendedReply && reply.delYn === "Y") {
          appendedReply.text("삭제된 댓글입니다.");
          appendedReply.css({
            color: "#F33",
          });
          continue;
        }
        // 이미 불러온 댓글인데, 탈퇴한 회원이 작성한 댓글일 경우
        else if (isAppendedReply && reply.memberVO.delYn === "Y") {
          appendedReply.text("탈퇴한 회원의 댓글입니다.");
          appendedReply.css({
            color: "#F33",
          });
          continue;
        }

        var appendedParentReply = $(
          ".reply[data-reply-id=" + reply.parentReplyId + "]"
        );

        /***********************새로운 댓글 추가*************************/

        //<div class="reply" data-reply-id="댓글번호" style="padding-left: (level - 1) * 40px">
        var replyDom = $("<div></div>");
        replyDom.addClass("reply");
        replyDom.attr("data-reply-id", reply.replyId);
        replyDom.data("reply-id", reply.replyId);
        replyDom.css({
          // "padding-left": (reply.level - 1) * 40 + "px", //= (? - 1) * 40px
          "padding-left": (reply.level1 === 1 ? 0 : 1) * 40 + "px",
          color: "#333",
        });

        if (reply.delYn === "Y") {
          replyDom.css({
            "background-color": "#F003",
            color: "#F33",
          });
          replyDom.text("삭제된 댓글입니다.");
        } else if (reply.memberVO.delYn === "Y") {
          replyDom.css({
            "background-color": "#F003",
            color: "#F33",
          });
          replyDom.text("탈퇴한 회원의 댓글입니다");
        } else {
          // <div class="author">사용자 명(사용자 이메일)</div>
          var authorDom = $("<div></div>");
          authorDom.addClass("author");
          authorDom.text(reply.memberVO.name + "(" + reply.email + ")");
          replyDom.append(authorDom);

          // <div class = "recommend-count">추천수: 실제추천수</div>
          var recommendCountDom = $("<div></div>"); // $("<div></div>") = 쉐도우 돔 생성
          recommendCountDom.addClass("recommend-count");
          recommendCountDom.text("추천수: " + reply.recommendCnt);
          replyDom.append(recommendCountDom);

          // <div class = "datetime">
          var datetimeDom = $("<div></div>"); //datetimeDom에는 crtDt, mdfyDt가 들어있을 것
          datetimeDom.addClass("datetime");

          //<span class="crtdt">등록: 등록날짜</span>
          var crtDtDom = $("<span></span>");
          crtDtDom.addClass("crtDt");
          crtDtDom.text("등록 : reply.crtDt");
          datetimeDom.append(crtDtDom);

          if (reply.crtDt !== reply.mofyDt) {
            // 수정날짜와 등록날짜가 다를 경우 수정이 됐다 라고 판단
            //<span class="crtdt">(수정: 수정날짜)</span>
            var mdfyDtDom = $("<span></span>");
            mdfyDtDom.addClass("mdfyDt");
            mdfyDtDom.text("(수정: " + reply.mdfyDt + ")");
            datetimeDom.append(mdfyDtDom);
          }
          replyDom.append(datetimeDom);

          //<pre class="content">댓글 내용</pre>
          var contentDom = $("<pre></pre>");
          contentDom.addClass("content");
          contentDom.text(reply.content);
          replyDom.append(contentDom);

          //댓글을 작성한 작성자와 현재 로그인되어있는 내 이메일이 동일할때만 수정할 수 있도록
          // -> sessionScope(jsp에서만 쓸수있음)
          var loginEmail = $("#login-email").text();
          var controlDom = $("<div></div>");

          // = 내가 작성한 이메일일 경우, 아래 3가지를 만들어서 controlDom에 넣어줌
          if (reply.email === loginEmail) {
            //<span class="modify-reply">수정</span>
            var modifyReplyDom = $("<span></span>");
            modifyReplyDom.addClass("modify-reply");
            modifyReplyDom.text("수정");
            modifyReplyDom.on("click", modifyReply);
            controlDom.append(modifyReplyDom);

            //<span class="modify-reply">삭제</span>
            var deleteReplyDom = $("<span></span>");
            deleteReplyDom.addClass("delete-reply");
            deleteReplyDom.text("삭제");
            deleteReplyDom.on("click", deleteReply);
            controlDom.append(deleteReplyDom);

            //<span class="modify-reply">답변</span>
            var reReplyDom = $("<span></span>");
            reReplyDom.addClass("re-reply");
            reReplyDom.text("답변하기");
            reReplyDom.on("click", reReply);
            controlDom.append(reReplyDom);
          } else {
            //<span class="modify-reply">추천하기</span>
            var recommendReplyDom = $("<span></span>");
            recommendReplyDom.addClass("recommend-reply");
            recommendReplyDom.text("추천하기");
            recommendReplyDom.on("click", recommendReply);
            controlDom.append(recommendReplyDom);

            var reReplyDom = $("<span></span>");
            reReplyDom.addClass("re-reply");
            reReplyDom.text("답변하기");
            reReplyDom.on("click", reReply);
            controlDom.append(reReplyDom);
          }

          replyDom.append(controlDom);
        }
        $(".reply-items").append(replyDom);
        // 일반 댓글은 reply-items의 자식으로 추가한다.
        if (!appendedParentReply.length > 0) {
          $(".reply-items").append(replyDom);
        }
        // 대댓글은 원 댓글의 자식으로 추가한다.
        else {
          appendedParentReply.append(replyDom);
        }
      }
    });
  };

  var boardId = $(".grid").data("id");
  loadReplies(boardId, 0); //0번페이지에 있는 reply를 다 가지고 와라

  $("#get-all-replies-btn").on("click", function () {
    loadReplies(boardId);
  });

  $("#btn-save-reply").on("click", function () {
    var reply = $("#txt-reply").val();
    var mode = $("#txt-reply").data("mode");
    var target = $("#txt-reply").data("target"); //target : 원 댓글

    if (reply.trim() !== "") {
      // 댓글등록?
      var body = { content: reply.trim() };
      var url = "/ajax/board/reply/" + boardId;

      if (mode === "re-reply") {
        //대댓글 등록
        body.parentReplyId = target;
      }

      if (mode === "modify") {
        //댓글수정
        url = "/ajax/board/reply/modify/" + target; //내가수정할 댓글의 번호
      }
    }

    $("#txt-reply").removeData("mode");
    $("#txt-reply").removeData("target");

    // url = 요청을보낼 url, body=요청본문데이터
    $.post(url, body, function (response) {
      // 요청이 성공하면 이어지는 함수가 실행
      var result = response.data.result;
      if (result) {
        loadReplies(boardId);
        $("#txt-reply").val(""); //댓글이 성공적으로 등록된 경우, 댓글을 입력하는 input 요소의 값을 비운다??
      } else {
        alert("댓글을 등록할 수 없습니다. 잠시 후 시도해주세요.");
      }
    });
  });

  $("#btn-cancel-reply").on("click", function () {
    $("#txt-reply").val("");
    $("#txt-reply").removeData("mode");
    $("#txt-reply").removeData("target");
  });
});
