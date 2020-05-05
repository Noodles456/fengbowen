function CollapseComment(e) {
    var id =e.getAttribute("data-id");
    var comments=$("#comment-"+id);
    var collapse=e.getAttribute("data-collapse");
    if (collapse){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
    e.classList.remove("active");
    }
    else {
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length !=1){
            comments.addClass("in");
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }
        else {
            $.getJSON("/comment/"+id,function (data) {
                $.each(data.data.reverse(),function (index,comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format("YYYY-MM-DD")
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}
function post() {
    var questionId=$("#question_id").val();
   var content= $("#comment_content").val();
  comment(questionId,1,content);
}
function comment(targetId,type,content) {

    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),

        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.messages);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=e09b85fc072325b7067f&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.messages);
                }
            }
        },
        dataType: "json"
    });
}

function comment2(e) {
    var commentId = e.getAttribute("data-id");
    var content= $("#input-"+commentId).val();
    comment(commentId,2,content)
}
function selectTag(e) {
var value=e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}
function showTag() {
    $("#show-tag").show();
}
function likeCount(id,qid,uid) {
    $.ajax({
        type: "POST",
        url: "/like",
        contentType: 'application/json',
        data: JSON.stringify({
            "id": id,
            "qid": qid,
            "uid": uid,
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#likec").html("点赞成功")
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.messages);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=e09b85fc072325b7067f&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } if(response.code == 2011) {
                    alert(response.messages);
                }
            }
        },
        dataType: "json"
    });
}
   function questionLike(e){
    var qid =e.getAttribute("data-qid");
    var uid=e.getAttribute("data-uid");
       var collapse=e.getAttribute("data-collapse");
       if (collapse){
           e.classList.remove("active");
       }else{
           e.classList.add("active");
           likeCount(0,qid,uid);
       }
}
function commentLike(e) {
    var id= e.getAttribute("data-id");
    var qid= e.getAttribute("data-qid");
    var uid= e.getAttribute("data-uid");
    var collapse=e.getAttribute("data-collapse");
    if (collapse){
       e.classList.remove("active");
   }
   else{
       e.classList.add("active");
        likeCount(id,qid,uid);
   }
   }

