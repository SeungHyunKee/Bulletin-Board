$().ready(function () {
  $("a.deleteMe").on("click", function () {
    $.get("/ajax/member/delete-me", function (response) {
      var next = response.data.next;
      location.href = next;
    });
  });
});

/*
 * 엔터키 눌렀을 때 form 전송안되도록 수정.
 * input의 data-submit 값이 true가 아닌 경우, 엔터키입력시 전송 안되도록 방지.
 */
$("form")
  .find("input")
  .on("keydown", function (event) {
    if (event.keyCode === 13) {
      var noSubmit = $(this).data("no-submit");
      if (noSubmit !== undefined) {
        event.preventDefault();
      }
    }
  });

// 공통으로 쓸수있는 search 함수.(함수를 공개시키기 위해서 ready밖으로 꺼내놓음. ready안쪽은 숨겨지므로!)
// form으로 보내고, get방식으로 전송?시킴
function search(pageNo) {
  var searchForm = $("#search-form");
  // var listSize = $("#list-size");
  $("#page-no").val(pageNo);

  searchForm.attr("method", "get").submit();
}
