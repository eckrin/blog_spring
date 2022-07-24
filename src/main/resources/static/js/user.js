let index = {
    init: function() {
        $("#btn_save").on("click", ()=> {
            this.save();
        });
        $("#btn_update").on("click", ()=> {
            this.update();
        });
//        $("#btn_login").on("click", ()=> {
//            this.login();
//        });
    },

    save: function() {
//        alert("user의 save함수 호출됨");
        let data = {
            username:$("#username").val(),
            password:$("#password").val(),
            email:$("#email").val()
        }
//        console.log(data);

        //ajax호출시 default가 비동기 호출
        //ajax통신을 이용해서 3개의 데이터를 json으로 변경
        //ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브텍트로 변환
        $.ajax({
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data), //js를 json으로 변경. http body 데이터 타입
            contentType: "application/json;charset=utf-8",
            dataType: "json" //응답 형태가 json형 문자열이라면 js로 변환해줌 >> 생략가능
        }).done(function(resp){
            alert("회원가입이 완료되었습니다.");
            console.log(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    update: function() {
//        alert("user의 save함수 호출됨");
        let data = {
            id:$("#id").val(),
            username:$("#username").val(),
            password:$("#password").val(),
            email:$("#email").val()
        }

        $.ajax({
            type: "PUT",
            url: "/user",
            data: JSON.stringify(data),
            contentType: "application/json;charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert("회원수정이 완료되었습니다.");
            console.log(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }

//    login: function() {
//        let data = {
//            username:$("#username").val(),
//            password:$("#password").val()
//        }
//
//        $.ajax({
//            type: "POST",
//            url: "/api/user/login",
//            data: JSON.stringify(data), //js를 json으로 변경. http body 데이터 타입
//            contentType: "application/json;charset=utf-8",
//            dataType: "json" //응답 형태가 json형 문자열이라면 js로 변환해줌 >> 생략가능
//        }).done(function(resp){
//            alert("로그인이 완료되었습니다.");
//            console.log(resp);
//            location.href="/";
//        }).fail(function(error){
//            alert(JSON.stringify(error));
//        });
//    }
}

index.init();