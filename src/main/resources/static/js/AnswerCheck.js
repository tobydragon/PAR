var responsesGivenText = [];

var numberOfQuestions;
var QuestionAnswers = [];

function getResponsesText() {
    return responsesGivenText;
}


function setQuestionLists() {
    numberOfQuestions= getNumberOfQuestions();
    QuestionAnswers = getQuestionAnswers();
}

function remove() {
    return " ";
}

function clearPage() {
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

function checkAndRecordAnswers(form){
    for(var i=0; i<numberOfQuestions; i++){
        var currentName= "q"+i;
        var currentAnswer= form[currentName].value;

        responsesGivenText.push(currentAnswer);

        var isCorrect;
        if (currentAnswer == QuestionAnswers[i]) {
            isCorrect = "Correct";
        } else {
            isCorrect = "Incorrect";
        }
        var displayAreaName= "questionCorrect"+i;
        document.getElementById(displayAreaName).innerHTML = displayCheck(isCorrect);
    }
}


function displayCheck(value) {
    if (value == "Correct") return '<font color=\"green\">Your answer is: ' + value + '</font>';
    if (value == "Incorrect") return '<font color=\"red\">Your answer is: ' + value + '</font>';
    if (value == "Unsure") return '<font color=\"#663399\">Your answer is: ' + value + '</font>';
}

function toggleShowState(toggableElement) {
    var changeElement = document.getElementById(toggableElement).classList;

    if (changeElement.contains("show") || changeElement.style.display == "block") {
        changeElement.remove("show");
        changeElement.add("hide");
    } else if (changeElement.contains("hide")) {
        changeElement.remove("hide");
        changeElement.add("show");
    }
}

function checkAnswers(form) {
    setQuestionLists();
    clearPage();
    checkAndRecordAnswers(form);
    generateResponseJSON();
}
