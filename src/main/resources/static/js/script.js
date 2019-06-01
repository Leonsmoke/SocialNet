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
            Array.from(addBtns).forEach((btn) => {
                btn.style.visibility="hidden";
        });
            Array.from(delBtns).forEach((btn) => {
                btn.style.visibility="visible";
        });
        } else {
            Array.from(addBtns).forEach((btn) => {
                btn.style.visibility="visible";
        });
            Array.from(delBtns).forEach((btn) => {
                btn.style.visibility="hidden";
        });
        }
    }
}

function createCommentArea(id){
    var mainElement = $(id).parent();
    if ($(id).parent().children()[5] != null){
        $('#CommentForm').remove();
    } else{
        $('#CommentForm').remove();
        var newForm = document.createElement("form");
        newForm.className="CommentForm";
        newForm.id="CommentForm";
        var newElement = document.createElement("textarea");
        newElement.cols = 40;
        newElement.rows = 5;
        var newBtn = document.createElement("input");
        newBtn.type="submit";
        newBtn.value="Send";
        newBtn.id="SendCommBtn";
        newForm.appendChild(newElement);
        newForm.appendChild(newBtn);
        mainElement.append(newForm);
    }

}
