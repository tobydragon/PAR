function getUserId() {
    UserID = document.getElementById("inputId").value;
    return UserID;
}

function login() {
    console.log('/imageTaskView?userId=' + getUserId());
    return location.replace('/imageTaskView?userId=' + getUserId());
}
