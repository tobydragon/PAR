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
        difficultyStr = createFillIn(question);
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
        difficultyStr = createFillIn(question);
        amountOfQuestions++;
    }

    QuestionAnswers.push(question.correctAnswer);
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

function generateImageURL(imageURL) {
    var displayURLThyme = imageURL.split('\\').pop().split('/').pop();
    displayURLThyme = displayURLThyme.substr(0, displayURLThyme.length - 1);
    imageURL = imageURL.substr(0, imageURL.length - 1);
    var displayURL = '<img src="' + imageURL + '" class="imgCenter" th:src="@{images/' + displayURLThyme + '}">';
    console.log(displayURL);
    console.log(imageURL);
    console.log(displayURLThyme);
    return displayURL;
}

function displayImageURL(imageURL) {
    document.getElementById('image').innerHTML = imageURL;
}

function pageDisplay() {
    var imageTaskJSON = readJson("api/nextImageTask");
    //Displays the image on the page at the appropriate tag
    displayImageURL(generateImageURL(imageTaskJSON.imageUrl));
    //Displays the questions at the tags
    for (var i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        generateQuestions(imageTaskJSON.taskQuestions[i]);
    }
}

function clearPage() {
    document.getElementById('questionSet').innerHTML = " ";
}

function changeQuestions() {
    clearPage();
    pageDisplay();
}
