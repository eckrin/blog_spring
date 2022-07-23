let index = {
    init: function() {
        $("#btn_save").on("click", ()=> {
            this.save();
        });
        $("#btn_delete").on("click", ()=> {
            this.deleteById(); //delete()는 예약어
        });
    },

    save: function() {
        let data = {
            title:$("#title").val(),
            content:$("#content").val(),
        }

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data),
            contentType: "application/json;charset=utf-8",
            dataType: "json"
        }).done(function(resp){
            alert("글쓰기가 완료되었습니다.");
            console.log(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    deleteById: function() {
        var id = $("#id").text();
        $.ajax({
            type: "DELETE",
            url: "/api/board/"+id,
            dataType: "json"
        }).done(function(resp){
            alert("삭제가 완료되었습니다.");
            console.log(resp);
            location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }

}

index.init();