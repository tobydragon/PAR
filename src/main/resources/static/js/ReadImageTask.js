var amountOfQuestions = 0;
var QuestionAnswers = [];
var QuestionIDs = [];
var UserID;


function getNumberOfQuestions() {
    return amountOfQuestions;
}

function getQuestionAnswers() {
    return QuestionAnswers;
}

function getQuestionIDs() {
    return QuestionIDs;
}

function getUserID() {
    return UserID;
}

function readJson(url) {
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

function generateQuestions(question) {
    var difficultyStr;
    if (question.difficulty == 1) {
        difficultyStr = createRadioQuestion(question);
        amountOfQuestions++;
    }
    if (question.difficulty == 2) {
        difficultyStr = createFillIn(question);
        amountOfQuestions++;
    }
    if (question.difficulty == 3) {
        difficultyStr = createFillIn(question);
        amountOfQuestions++;
    }
    if (question.difficulty == 4) {
        difficultyStr = createRadioQuestion(question);
        amountOfQuestions++;
    }
    displayQuestions(difficultyStr);

}

function displayQuestions(displayHTML) {
    document.getElementById("questionSet").innerHTML += displayHTML;
}

function createRadioQuestion(json) {
    var question = "<p>" + json.questionText + "</p>";
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question += '<br> <input type="radio" name="' + ("q" + (getNumberOfQuestions())) + '" value="';
        question = question + json.possibleAnswers[i] + '">' + json.possibleAnswers[i] + '<br> <i id="' + "questionCorrect" + (getNumberOfQuestions()) + '"></i>';
    }
    return question;
}

function createFillIn(json) {
    var question = "<p>" + json.questionText + '</p> <input name="' + ("q" + (getNumberOfQuestions())) + '" list="' + ("list" + getNumberOfQuestions()) + '"/> <datalist id="' + ("list" + getNumberOfQuestions()) + '">';
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option value="' + json.possibleAnswers[i] + '"/>';
    }
    question += '</datalist>';
    question += '<i id="' + "questionCorrect" + (amountOfQuestions) + '"></i>';

    return question;
}

function clearPage() {
    document.getElementById('questionSet').innerHTML = " ";
}

function changeQuestions() {
    clearPage();
    pageDisplay();
}

function displayImageURL(imageURL) {
    return '<img class="imgCenter" src="' + imageURL + '">';
}

function pageDisplay() {
    var imageTaskJSON = readJson("api/nextImageTask");

    //Displays the image on the page at the appropriate tag
    //displayImage(imageTaskJSON.imageURL);
    displayImageURL(imageTaskJSON.imageURL);
    //Displays the questions at the tags
    for (var i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        generateQuestions(imageTaskJSON.taskQuestions[i]);
    }
}
