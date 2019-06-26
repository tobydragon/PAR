var UserId= null;

function setUserId() {
    UserID = document.getElementById("inputId").value;
    return UserID;
}

function login() {
    setUserId()
    console.log('/imageTaskView?userId=' + UserId);
    return location.replace('/imageTaskView?userId=' + UserId);
}
