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

function enterSubmit() {
    console.log("enterSubmit");
    document.getElementById('inputId').onkeydown = function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
            change();
        }

    }
}
