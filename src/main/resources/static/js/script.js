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
    }
}



