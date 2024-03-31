$().ready(function () {
  $("#btn-login").on("click", function () {
    $(".error").remove(); //클래스가 에러인것을 다 지워라(에러메세지 여러개 안뜨게하게 위함)
    $("div.grid").removeAttr("style");

    $.post(
      "/member/login",
      {
        email: $("#email").val(),
        password: $("#password").val(),
        nextUrl: $("#next").val(),
      },
      function (response) {
        var errors = response.data.errors;
        var errorMessage = response.data.errorMessage;
        var next = response.data.next;

        //파라미터 유효성 검사에 실패했을 경우
        if (errors) {
          //데이터가 있다면

          for (var key in errors) {
            //객체리터럴의 for문에서의 i에는 키가 할당(배열에서의 i에는 인덱스가 할당)
            //console.log(key);
            // console.log(errors.key); <-이렇게쓰면 key라는 키워드를 찾아옴
            //console.log(errors[key]); //<- 변수를 통해 가져오는 올바른 방법
            var errorDiv = $("<div></div>");
            errorDiv.addClass("error"); //-> 즉, <div class="error"></div>만들어라

            var values = errors[key]; //key를 주고
            for (var i in values) {
              //index를 줌
              var errorValue = values[i];

              var error = $("<div></div>");
              error.text(errorValue);

              errorDiv.append(error);
            }

            //jquery에서 shadow dom 추가할수있는 방법 : append, after, ..
            $("input[name=" + key + "]").after(errorDiv);
          }

          if (errors.email && errors.password) {
            //클래스 지정
            // $("div.grid").addClass("validator-fail-both");
            var emailFailCount = errors.email.length;
            var passwordFailCount = errors.password.length;

            // if (emailFailCount > 1 || passwordFailCount > 1) {
            //Inline-Style 지정
            $("div.grid").css({
              "grid-template-rows":
                "28px " +
                21 * emailFailCount +
                "px 28px " +
                21 * passwordFailCount +
                "px 1fr",
            });
            // }
          } else if (errors.email) {
            // $("div.grid").addClass("validator-fail-email");
            var emailFailCount = errors.email.length;
            $("div.grid").css({
              "grid-template-rows":
                "28px " + 21 * emailFailCount + "px 28px 1fr",
            });
          } else if (errors.password) {
            // $("div.grid").addClass("validator-fail-password");
            var passwordFailCount = errors.password.length;
            $("div.grid").css({
              "grid-template-rows":
                "28px 28px " + 21 * passwordFailCount + "px 1fr",
            });
          }
        }

        // 파라미터 유효성검사는 패스
        // 이메일이나 패스워드가 잘못된 경우
        if (errorMessage) {
          //   console.log(errorMessage); //이메일또는 비밀번호가 잘못되었습니다.
          var errorDiv = $("<div></div>");
          errorDiv.addClass("error");
          errorDiv.text(errorMessage);
          //id가 login form 다음인곳에 넣어줌
          $("#loginForm").after(errorDiv);
        }

        // 정상적으로 로그인에 성공한 경우
        if (next) {
          location.href = next; //정상적으로 로그인할 경우 서버가 돌려주는 경로로 이동을 해라
        }
      }
    );
  });
});

/*
Browser -------(데이터 전송)---------> Server

데이터 전송의 방법
1. query string   ->   ? key=value & key = value
2. form
input name = "email"  => email
textarea name = "desc"  => desc
select name = "type"  => type
*/
