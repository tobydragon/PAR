class ReadImageTask {
    constructor(amountOfQuestions, QuestionTypes, QuestionAnswers, QuestionIDs, UserID) {

        this.amountOfQuestions = amountOfQuestions;
        this.QuestionTypes = QuestionTypes;
        this.QuestionAnswers = QuestionAnswers;
        this.QuestionIDs = QuestionIDs;
        this.UserID = UserID;


    }
}


function getUserID() {
    return this.UserID;
}

function getQuestionTypes() {
    return this.QuestionTypes;
}

function getQuestionAnswers() {
    return this.QuestionAnswers;
}

function getQuestionIDs() {
    return this.QuestionIDs;
}

function readJson(url) {
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

function generateQuestions(question) {
    var readImageTaskObj = new ReadImageTask(0, [], [], [], 0);
    var difficultyStr;
    if (question.difficulty == 1) {
        difficultyStr = setPlane(readImageTaskObj);
        readImageTaskObj.amountOfQuestions++;
    }
    if (question.difficulty == 2) {
        difficultyStr = createFillIn(readImageTaskObj, question);
        readImageTaskObj.amountOfQuestions++;
    }
    if (question.difficulty == 3) {
        difficultyeStr = createFillIn(readImageTaskObj, question);
        readImageTaskObj.amountOfQuestions++;
    }
    if (question.difficulty == 4) {
        difficultyStr = createZoneQuestion(readImageTaskObj, question);
        readImageTaskObj.amountOfQuestions++;
    }
    displayQuestions(difficultyStr);

}

function displayQuestions(displayHTML) {
    document.getElementById("questionSet").innerHTML = displayHTML;
}

function createRadioQuestion(obj, json) {
    var question = "<p>" + json.questionText + "</p>";
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question += '<br> <input type="radio" name="' + ("q" + (obj.amountOfQuestions)) + '" value="';
        question = question + json.possibleAnswers[i] + '">' + json.possibleAnswers[i] + '<br> <i id="' + "questionCorrect" + (obj.amountOfQuestions) + '"></i>';
    }
    return question;
}

function createFillIn(obj, json) {
    var question = "<p>" + json.questionText + '</p> <input name="' + ("q" + (obj.amountOfQuestions)) + '" list="' + ("list" + obj.amountOfQuestions) + '"/> <datalist id="' + ("list" + obj.amountOfQuestions) + '">';
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option value="' + json.possibleAnswers[i] + '"/>';
    }
    question += '</datalist>';
    question += '<i id="' + "questionCorrect" + (obj.amountOfQuestions) + '"></i>';

    return question;
}

function changeQuestions() {
    clearpage();

    var question = readJson("api/nextImageTask")
    for (var i = 0; i < question.length; i++) displayQuestions(generateQuestions(question.get(i)));
}

function displayImageURL(imageURL) {
    return '<img class="imgCenter" src="' + imageURL + '">';
}

function pageDisplay() {
    var i;
    var imageTaskJSON = readJson("api/nextImageTask");
    var qArray = new Array[imageTaskJSON.taskQuestions.length];
    //Displays the image on the page at the appropriate tag
    document.getElementById("image").innerHTML = displayImageURL(imageTaskJSON.taskQuestions[0].imageURL);
    //Displays the questions at the tags
    for (i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        qArray.add(imageTaskJSON.taskQuestions[i]);
        readQuestion(qArray[i]);
    }
}
