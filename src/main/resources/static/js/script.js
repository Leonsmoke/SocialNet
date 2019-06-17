function checkPasswordMatch() {
    var password = $("#pass").val();
    var confirmPassword = $("#passCheck").val();

    if (password != confirmPassword) {
    $("#checkPassMatch").html("Password are different!");
    $("#checkPassMatch").color="red";
    $('#regBut').prop('disabled',true);
}
    else {
        $("#checkPassMatch").html("Password are match!");
        $('#regBut').prop('disabled',false);
    }
}


$(document).ready(function () {
    $("#pass, #passCheck").keyup(checkPasswordMatch);

});

function checkTypeProfile(){
    var text = $("#textFriend").text();
    if (text=="It's your Friend" || text == "You send friend request"){
        $('#addFriendButton').remove();
        $('#updateAvatar').remove();
        document.getElementById("deleteFriendButton").style.visibility="visible";

    } else if (text=="It's not your friend" || text == "It's requested you for friend"){
        document.getElementById("addFriendButton").style.visibility="visible";
        $('#deleteFriendButton').remove();
        $('#updateAvatar').remove();
    } else if (text == "It's your profile"){
        $('#deleteFriendButton').remove();
        $('#addFriendButton').remove();
        document.getElementById("editProfile").style.visibility="visible";
        document.getElementById("updateAvatar").style.visibility="visible";
    }
}

function checkPageFriend(){
    var text = $("#page-type").text();
    var pageId = $("#pageId").text();
    var userId = $("#userId").text();
    var addBtns= document.getElementsByClassName("addFriendBtn");
    var delBtns= document.getElementsByClassName("deleteFriendBtn");

    if (pageId == userId){
        document.getElementById("AllFriends").style.visibility="visible";
        document.getElementById("IncomingRequest").style.visibility="visible";
        document.getElementById("OutcomingRequest").style.visibility="visible";
        if (text=="main" || text=="out"){
            $('.addFriendBtn').remove();
        } else {
            $('.deleteFriendBtn').remove();
        }
    } else {
        $('.addFriendBtn').remove();
        $('.deleteFriendBtn').remove();
    }
}

function createCommentArea(id){
    var mainElement = $(id).parent().parent();

    if ($(id).parent().parent().children().length>=2){
        $('#CommentForm').remove();
    } else{
        $('#CommentForm').remove();
        //getAllComments(id);
        var newForm = document.createElement("form");
        newForm.className="CommentForm";
        newForm.id="CommentForm";
        var newElement = document.createElement("textarea");
        newElement.cols = 40;
        newElement.rows = 5;
        newElement.id="commentText";
        var newBtn = document.createElement("input");
        newBtn.className="sendCommButton";
        newBtn.type="button";
        newBtn.value="Send";
        newBtn.id="SendCommBtn";
        $(newBtn).on('click', function onclick() {
            sendCommentPost(id);
        });

        newForm.appendChild(newElement);
        newForm.appendChild(newBtn);
        mainElement.append(newForm);
    }




}

function sendCommentPost(param) {
    var request = new XMLHttpRequest();
    var url = "/user/feed/addComment";
    var post_id = $(param).parent().parent().attr("id");
    var wall_id = $(param).parent().parent().attr("classid");
    if (wall_id==-1){
        wall_id = $(param).parent().parent().attr("frame");
        url="/community/addComment";
    }
    var params = "post_id="+ post_id +
        "&wall_id=" + wall_id +
        "&text=" + $(param).parent().parent().children('#CommentForm').children('#commentText').attr("value");
    request.open("POST", url, true);
    request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    request.addEventListener("readystatechange", () => {

        if (request.readyState == 4 && request.status==200){
        document.location.reload();
    }
});

    request.send(params);
}


function commentBtn(id) {
    createCommentArea(id);
    var post_id = $(id).parent().parent().attr("id");
    getAllComments(post_id);
}
function workWithCommunityPage(){
    var isAdmin = $('#leaveCommunityBtn').parent().attr('classid');
    var isJoined = $('#joinCommunityBtn').parent().attr('classid');
    if (isAdmin=="falsee"){
        $('#updateCommAvatar').remove();
    }
    if (isJoined=="truee"){
        $('#joinCommunityBtn').remove();
    } else {
        $('#leaveCommunityBtn').remove();
    }
}

function addAdminLink(role) {
    if (role=="ADMIN"){
        var menu = $('#topMenu');
        var link = document.createElement("li");
        var ref = document.createElement("a");
        ref.href="/admin/panel";
        ref.text="Admin's panel";
        link.appendChild(ref);
        menu.append(link);
    }
}