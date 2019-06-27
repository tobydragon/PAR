var User;

function setUserId() {
    User = document.getElementById("inputId").value;
    return User;
}

function change() {
    setUserId();
    try{
        return location.replace('/imageTaskView?userId=' + User);
    } catch(Exception ) {
        window.onerror = function (msg) {
            location.replace('/error?message='+msg);
        }
    }
}
