var reponsesGiven= [];

var QuestionTypes= [];
var QuestionAnswers= [];

function setQuestionLists(){
    QuestionTypes= getQuestionTypes();
    QuestionAnswers= getQuestionAnswers();
}

function remove() {
    return " ";
}

function clearPage(){
    document.getElementById('plane').innerHTML = remove();
    document.getElementById('structure0').innerHTML = remove();
    document.getElementById('structure1').innerHTML = remove();
    document.getElementById('structure2').innerHTML = remove();
    document.getElementById('structure3').innerHTML = remove();
    document.getElementById('attachment0').innerHTML = remove();
    document.getElementById('attachment1').innerHTML = remove();
    document.getElementById('attachment2').innerHTML = remove();
    document.getElementById('attachment3').innerHTML = remove();
    document.getElementById('zone').innerHTML = remove();


    document.getElementById('planeCorrect').innerHTML = remove();
    document.getElementById('structure0Correct').innerHTML = remove();
    document.getElementById('structure1Correct').innerHTML = remove();
    document.getElementById('structure2Correct').innerHTML = remove();
    document.getElementById('structure3Correct').innerHTML = remove();
    document.getElementById('attachment0Correct').innerHTML = remove();
    document.getElementById('attachment1Correct').innerHTML = remove();
    document.getElementById('attachment2Correct').innerHTML = remove();
    document.getElementById('attachment3Correct').innerHTML = remove();
    document.getElementById('zoneCorrect').innerHTML = remove();
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

function checkAnswers(form){
    setQuestionLists();
    clearPage();

}
