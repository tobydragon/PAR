var User;

function setUserId() {
    User = document.getElementById("inputId").value;
    console.log(User);
    return User;
}

function login() {
    setUserId();
    return location.replace('/imageTaskView?userId=' + User);
}

function enterSubmit() {
    document.getElementById('inputId').onkeydown = function (e) {
        //13 is enter key code
        if (e.keyCode == 13) {
            //preventDevault() cancells current action, in this case the page refreshing and allows the login function to be called
            e.preventDefault();
            login();
        }

    }
}
