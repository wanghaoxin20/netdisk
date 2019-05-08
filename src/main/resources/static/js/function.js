$(document).ready(function () {

    var base_url = "/netdisk/";
    var NetDisk_url = "/NetDisk/";

    var crtpath = "/";
    var fileData = null;

    update(crtpath);
    /******************************************************** */
    //事件绑定
    $('[data-toggle="tooltip"]').tooltip();

    //关于
    $("#about").click(function () {
        var ma = $("#modal_add");
        ma.html("");
        ma.append(ModalContent("关于作者", "about"));
        $("#modal").modal('show');
    });

    //新建文件夹
    $("#btn_newdir").click(function () {
        var ma = $("#modal_add");
        ma.html("");
        ma.append(ModalContent("新建文件夹", "newdir"));
        $("#modal").modal('show');
    });

    //上传文件
    $("#btn_upload").click(function () {
        var ma = $("#modal_add");
        ma.html("");
        ma.append(ModalContent("上传文件", "uploadfile"));
        $("#modal").modal('show');
    });

    //选择文件
    $("#file").change(function () {
        var file = document.getElementById("file").files;
        var html = "";
        for (var i in file) {
            html += file[i].name + "<br>";
        }
        $("#filename").html(html);
    });

    //登出
    $("#login_out").click(function () {
        axios
            .post(
                base_url + "loginout"
            )
            .then(function (res) {
                //成功请求后返回数据的处理
                if (res.data.status == 200) {
                    window.location = "/";
                } else if (res.data.status == 400) {
                    alert("error:" + res.data.msg);
                }
            })
            .catch(function (error) {
                alert("error" + error);
            });
    });

    function update(path) {
        axios
            .post(
                base_url + "files",
                {path: path},
                {headers: {"Content-Type": "application/json;charset=utf-8"}}
            )
            .then(function (res) {
                //成功请求后返回数据的处理
                if (res.data.status == 200) {
                    crtpath = path;
                    fileData = res.data.data;
                    updateIndex();
                    updateTableView(fileData);
                } else if (res.data.status == 400) {
                    alert("error:" + res.data.msg);
                }
            })
            .catch(function (error) {
                alert("error" + error);
            });
    }

    //更新表中数据视图
    function updateTableView(fileData) {
        $("#tbody").html("");
        var tbody = $("#tbody");
        for (i in fileData) {
            tbody.append(rowDataHtml(fileData[i], i));
        }
    }

    //更新索引
    function updateIndex() {
        $("#index").html("");
        var index = $("#index");
        var paths = getpaths();
        index.append(breadcrumbItem("/", ""));
        for (i in paths) {
            index.append(breadcrumbItem(paths[i], "", i));
        }
    }

    //进入目录
    function cdTo(index) {
        var paths = getpaths();
        var p = "";
        //拼接路径path
        for (var i = 0; i <= index; i++) {
            p = p + "/" + paths[i];
        }
        update(p + "/");
    }

    //获取path
    function getpaths() {
        var strs = crtpath.split("/");
        var reg = /^\/+|\s+$/;
        var paths = [];
        var j = 0;
        for (i in strs) {
            if (!reg.test(strs[i]) && strs[i] != "") {
                paths[j] = strs[i];
                j++;
            }
        }
        return paths;
    }

    //上传文件
    function uploadFile() {
        var file = document.getElementById("file").files;
        if (file == null) {
            alert("文件选择为空");
        } else {
            $("#progressb").css("display", "");
            var form = new FormData();
            form.append("path", crtpath);
            for (var i in file) {
                form.append("file", file[i]);
            }
            $.ajax({
                url: base_url + "uploadfile",
                type: "POST",
                data: form,
                processData: false,
                contentType: false,
                xhr: function () {        //ajax进度条
                    var xhr = $.ajaxSettings.xhr();
                    if (xhr.upload) {
                        xhr.upload.addEventListener("progress", function (e) {
                            var loaded = e.loaded; //已经上传大小情况
                            var tot = e.total; //附件总大小
                            var per = Math.floor(100 * loaded / tot); //已经上传的百分比
                            $(".modal-title")[0].innerHTML = "上传文件[" + per + "%]";
                            $("#progress").css('width', per + '%');
                        }, false);
                        return xhr;
                    }
                },
                success: function (res) {
                    if (res.status == 200) {
                        $("#mes").text("上传成功");
                        update(crtpath);
                    } else {
                        $("#mes").text("上传失败[error:" + res.msg + "]");
                    }
                    $("#file").val("");
                },
                error: function (error) {
                    alert("error:" + error)
                }
            })
        }
    }

    //新建文件夹
    function mkdir() {
        var dirname = $("#dirname").val();
        var reg = /^\s+$/;
        if (reg.test(dirname) || dirname == "") {
            alert("文件夹名不能为空");
        } else {
            axios
                .post(base_url + "mkdir", {
                    path: crtpath,
                    dirname: dirname,
                })
                .then(function (res) {
                    //成功请求后返回数据的处理
                    if (res.data.status == 200) {
                        fileData = res.data.data;
                        update(crtpath);
                        $("#modal").modal('hide');
                    } else if (res.data.status == 400) {
                        alert("error:" + res.data.msg);
                    }
                    $("#dirname").val("");
                })
                .catch(function (error) {
                    alert("error" + error);
                });
        }
    }

    //删除
    function del(index) {
        var f = fileData[index];
        axios
            .post(base_url + "deldir", {
                path: crtpath,
                name: f.name,
            })
            .then(function (res) {
                //成功请求后返回数据的处理
                if (res.data.status == 200) {
                    update(crtpath);
                    $("#modal").modal('hide');
                } else if (res.data.status == 400) {
                    alert("error:" + res.data.msg);
                }
            })
            .catch(function (error) {
                alert("error" + error);
            });
    }

    /******************************************************** */
    //组件相关

    //获取每一行的数据视图html
    function rowDataHtml(rowData, index) {
        var tr = $("<tr></tr>");
        var tdName = $("<td></td>");
        var a = $("<a href=\"javascript:void(0)\">" + rowData.name + "</a>");
        if (!rowData.isFile) {
            a.click(function () {
                update(crtpath + fileData[index].name + "/");
            });
        } else {
            a.addClass("text-info");
        }
        tdName.append(a);

        var d = $("<i class=\"fa fa-trash-o fa-lg float-right\"  data-toggle=\"tooltip\" title=\"删除文件\"></i>");
        // var d = $("<a style=\"float: right;\" href='javascript:void(0)'>删除</a>");
        d.click(function () {
            var ma = $("#modal_add");
            ma.html("");
            ma.append(ModalContent("提示", "del", index));
            $("#modal").modal('show');
        });

        var tdSize = $("<td>" + rowData.size + "</td>");
        tdSize.append(d);

        if (rowData.isFile) { //如果为文件 添加监听
            var download = $("<i class=\"fa fa-cloud-download fa-lg float-right mx-3\" data-toggle=\"tooltip\" title=\"下载\"></i>")
            download.click(function () {
                window.open(
                    NetDisk_url + "?filename=" + crtpath + "/" + rowData.name
                );
            })
            tdSize.append(download);
        }
        tr.append(tdName, tdSize);
        return tr;
    }

    //面包导航
    function breadcrumbItem(text, active, index) {
        var x = $("<li class=\"breadcrumb-item " + active + "\"><a href=\"javascript:void(0)\">" + text + "</a></li>");
        x.click(function () {
            cdTo(index);
        });
        return x;
    }

    //模态框内容
    function ModalContent(titlename, type, index) {
        var modalcontent = $("<div class=\"modal-content\"></div>");
        modalcontent.append(modalHeader(titlename), modalBody(type, index), modalFooter(type, index));
        return modalcontent;
    }

    //模态框header
    function modalHeader(titlename) {
        var container = $("<div class=\"modal-header\"></div>");
        var title = $("<h3 class=\"modal-title\">" + titlename + "</h3>");
        var close = $("<button class=\"close\" data-dismiss=\"modal\"><span>&times;</span></button>");
        container.append(title, close);
        return container;
    }

    //模态框body
    function modalBody(type, index) {
        var container = $("<div class=\"modal-body\"></div>");
        if (type == "newdir") {
            var inputDirname = $("<div class=\"form-group\"><label for=\"dirname\">文件夹名</label><input id=\"dirname\" class=\"form-control\" type=\"text\"></div>");
            container.append(inputDirname);
        } else if (type == "uploadfile") {
            var group = $("<div class=\"form-group\"></div>");
            var btn = $("<button type=\"button\" class=\"btn btn-info\">选择文件</button>");
            var lable = $("<label id=\"filename\"></label>");
            var progress = $("<div id='progressb' class=\"progress\" style='display:none;'></div>");
            btn.click(function () {
                $("#file").click();
            });
            $("#file").change(function () { //选择文件
                $(".modal-title")[0].innerHTML = "上传文件";
                lable.text(document.getElementById("file").files[0].name);
                var progressb = $("#progressb");
                progressb.html("");
                $("#mes").text("");
                progressb.append($("<div id='progress' class=\"progress-bar bg-info\" style=\"width:0%\">"));
                progressb.css("display", "none");
            });
            group.append(btn, lable);
            container.append(group, progress);
        } else if (type == "del") {
            var p = $("<p>确定要删除文件[" + fileData[index].name + "]吗</p>");
            container.append(p);
        } else if (type == "about") {
            var info = $("<dl>" +
                        "<dt>Email</dt><dd><i class='fa fa-hand-o-right'></i><a href='javascript:void(0)' class='text-info'> wanghaoxin@nexuslink.cn</a></dd>" +
                        "<dt>Github</dt><dd><i class='fa fa-hand-o-right'></i>&nbsp;-&nbsp;<a href='https://github.com/wanghaoxin20' class='text-dark' target='_blank'><i class='fa fa-github fa-2x'></i></a></dd>" +
                "</dl>");
            container.append(info);
        }

        return container;
    }

    //模态框footer
    function modalFooter(type, index) {
        var container = $("<div class=\"modal-footer\"></div>")
        var mes = "<p id='mes' class='float-left'></p>"
        var btn = $("<button type=\"button\" class=\"btn btn-secondary\">确定</button>");
        if (type == "newdir") {
            btn.click(function () {
                mkdir();
            });
        } else if (type == "uploadfile") {
            btn.click(function () {
                uploadFile();
            })
        } else if (type == "del") {
            btn.click(function () {
                del(index);
            })
        }
        container.append(mes, btn);
        return container;
    }

});