$().ready(function () {
  // ex) 6페이지 보다가 10,20,30,50,100 값 바뀌면 1페이지로 돌아가는 기능 구현을 위함
  $("#list-size").on("change", function () {
    search(0);
  });

  //검색버튼이 click되면 함수를 수행하라는 것
  $("#search-btn").on("click", function () {
    search(0);
  });

  $("#cancel-search-btn").on("click", function () {
    location.href = "/board/search";
  });

  $("#uploadExcelfile").on("click", function () {
    $("#excelfile").click();
  });

  /**파일이 선택되면 수행해라~ */
  $("#excelfile").on("change", function () {
    //선택된 파일의 정보를 출력
    var file = $(this)[0].files[0]; //input파일의 첫번째 파일
    var filename = file.name;

    if (!filename.endsWith(".xlsx")) {
      alert("엑셀파일을 선택해주세요!!");
      //엑셀파일을 선택하지 않았을경우 함수실행을 종료시킴
      return;
    }

    //엑셀파일을 잘 선택한 경우
    //파일을 서버로 전송시킨다
    var formData = new FormData();
    //formData에 파일정보를 첨부시킨다
    formData.append("excelFile", file);

    //파일전송은 $.post로 할 수 없다.
    // short hand가 아닌, 전체문장을 써야된다
    $.ajax({
      url: "/ajax/board/excel/write", //요청을 보낼 주소
      type: "POST", //요청을 보낼 httpMethod
      data: formData, //요청을 보낼 데이터 (formData)
      processData: false,
      contentType: false,
      success: function (response) {
        console.log(response);
        var data = response.data;
        if (data.result && data.next) {
          location.href = data.next;
        }
      },
    });
  });
});
