function remove() {
    return " ";
}

function readJson(url) {
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

function validateCheck(responses) {
    var answerList = new Array(responses.length);
    var i;
    for (i = 0; i < responses; i++) {
        if (document.getElementById(responses[i]).value == "Correct") {
            answerList[i] = "Correct";
        } else if (document.getElementById(responses[i]).value == "Incorrect") {
            answerList[i] = "Incorrect";
        } else if (document.getElementById(response[i]).value == "Unsure") {
            answerList[i] = "Unsure";
        }
    }
    return answerList;
}


function displayCheck(value) {
    if (value == "Correct") return '<font color=\"green\">Your answer is: ' + value + '</font>';
    if (value == "Incorrect") return '<font color=\"red\">Your answer is: ' + value + '</font>';
    if (value == "Unsure") return '<font color=\"#663399\">Your answer is: ' + value + '</font>';
}

function toggleShowState(element) {
    if (document.getElementById(element).classList.contains(show) || document.getElementById(element).style.display == "block") {
        document.getElementById(element).classList.remove(show);
        document.getElementById(element).classList.add(hide);
    } else if (document.getElementById(element).classList.contains(hide)) {
        document.getElementById(element).classList.remove(hide);
        document.getElementById(element).classList.add(show);
    }
}
