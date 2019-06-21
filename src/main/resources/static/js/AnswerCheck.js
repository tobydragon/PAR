var responsesGivenText = [];

var numberOfQuestions;
var QuestionAnswers = [];

function getResponsesText() {
    return responsesGivenText;
}

function setQuestionLists() {
    numberOfQuestions = getNumberOfQuestions();
    QuestionAnswers = getQuestionAnswers();
}

function checkAndRecordAnswers() {
    var form = document.getElementById("form1")
    for (var i = 0; i < numberOfQuestions; i++) {
        var currentName = "q" + i;
        var currentAnswer = form[currentName].value;

        responsesGivenText.push(currentAnswer);

        var isCorrect;
        if (currentAnswer == QuestionAnswers[i]) {
            isCorrect = "Correct";
        } else {
            isCorrect = "Incorrect";
        }
        var displayAreaName = "questionCorrect" + i;
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

function checkAnswers() {
    //get list of question answers and number of questions from readImageTask
    setQuestionLists();
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    checkAndRecordAnswers();
    //call CreateResponse to send answers back to the server
    generateResponseJSON();
}
