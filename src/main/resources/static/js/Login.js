var User;

function setUserId() {
    User = document.getElementById("inputId").value;
    console.log(User);
    return User;
}

function change() {
    setUserId();
    return location.replace('/imageTaskView?userId=' + User);
}
