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
        document.getElementById("addFriendButton").style.visibility="hidden";
        document.getElementById("deleteFriendButton").style.visibility="visible";

    } else if (text=="It's not your friend" || text == "It's requested you for friend"){
        document.getElementById("addFriendButton").style.visibility="visible";
        document.getElementById("deleteFriendButton").style.visibility="hidden";
    } else if (text == "It's your profile"){
        document.getElementById("addFriendButton").style.visibility="hidden";
        document.getElementById("deleteFriendButton").style.visibility="hidden";
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

function splitPost(post){
    var arrayText = post.split('$');
    return arrayText;
}