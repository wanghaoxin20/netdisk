$(document).ready(function () {

    var base_url = "/user";
    
    $("#password").keydown(function (e) { //回车监听
        if (e.keyCode == 13) {
            $("#login").click();
        }
    })
    
    $("#login").click(function () {
       var username = $("#username").val();
       var password = $("#password").val();
       login(username, password);
    });

    function login(username, password) {
        axios
            .post(
                base_url + "/login",
                {
                    username: username,
                    password: password
                },
                { headers: { "Content-Type": "application/json;charset=utf-8" } }
            ).then(function (res) {
                //成功请求后返回数据的处理
                if (res.data.status == 200) {
                    window.location = "/netdisk";
                } else if (res.data.status == 300) {
                    alert("账号密码错误")
                } else if (res.data.status == 400) {
                    alert("error:" + res.data.msg);
                }
            })
            .catch(function (error) {
                alert("error" + error);
            });
    }
})