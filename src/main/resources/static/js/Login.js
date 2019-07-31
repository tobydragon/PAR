var User;
var bottomText = ["Ready... Set... PAR!", "You're going to be amazing!", "A PAR employee will never ask for your password", "Time to rock the show!"];


function getRandomNumber(num) {
    var rand = Math.floor(Math.random() * num)
    return rand;
}

function setBottomText() {
    var currNum = getRandomNumber(bottomText.length);
    var currText = bottomText[currNum];
    document.getElementById("idHelp").innerHTML = currText;
}

function setUserId() {
    User = document.getElementById("inputId").value;
    return User;
}

function login() {
    setUserId();
    try {
        return location.replace('/imageTaskView?userId=' + User);
    } catch (Exception) {
        window.onerror = function (msg) {
            location.replace('/error?message=' + msg);
        }
    }
}

function enterSubmit() {
    document.getElementById('inputId').onkeydown = function (e) {
        //13 is enter key code
        if (e.keyCode == 13) {
            //preventDevault() cancels current action, in this case the page refreshing and allows the login function to be called
            e.preventDefault();
            login();
        }

    }
}
