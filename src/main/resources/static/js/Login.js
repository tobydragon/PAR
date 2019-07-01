var User;
var bottomText = ["Ready... Set... PAR!", "You're going to be amazing!", "A PAR employee will never ask for your password", "Are you a dedicated horse person?", "Are you ready to rumble?????"];

function tableInputKeyPress(e) {
    e = e || window.event;
    var key = e.keyCode;
    if (key == 13) //Enter
    {
        change();
        return true; //return true to submit, false to do nothing
    }
}

function getRandomNumber(num){
    var rand= Math.floor(Math.random() * num)
    return rand;
}

function setBottomText(){
    var currNum= getRandomNumber(bottomText.length);
    var currText= bottomText[currNum];
    document.getElementById("idHelp").innerHTML = currText;
}

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
