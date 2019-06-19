class ReadImageTask {
    constructor(amountOfQuestions, QuestionAnswers, QuestionIDs, UserID) {

        this.amountOfQuestions = amountOfQuestions;
        this.QuestionAnswers = QuestionAnswers;
        this.QuestionIDs = QuestionIDs;
        this.UserID = UserID;


    }
}


function getUserID() {
    return this.UserID;
}

function getNumberOfQuestions() {
    return this.amountOfQuestions;
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

function generateQuestion(question) {
    var readImageTaskObj = new ReadImageTask(0, [], [], [], 0);
    var difficultyStr;
    if (question.difficulty == 1) {
        difficultyStr = setPlane();
    }
    if (question.difficulty == 2) {
        difficultyStr = createFillIn(question);
    }
    if (question.difficulty == 3) {
        difficultyeStr = createFillIn(question);
    }
    if (question.difficulty == 4) {
        difficultyStr = createZoneQuestion(question);
    }
    displayQuestion(difficultyStr);

}

function displayQuestion(displayHTML) {
    document.getElementById("questionSet").innerHTML = displayHTML;
}

function setPlane() {
    return '<p>What plane is this?</p> <input type="radio" name="' + "q" + (this.amountOfQuestions++) + '" value="Lateral"> Lateral<br> <input type="radio" name="' + (this.amountOfQuestions) + '" value="Transverse"> Transverse<br> <input type="radio" name="' + (this.amountOfQuestions) + '" value="Unsure"> I do not know<br> <i id="' + "questionCorrect" + (this.amountOfQuestions) + '"></i>';
}

function createZoneQuestion(json) {
    var question = "<p> What zone is this? </p>";
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question += '<br> <input type="radio" name="' + (this.amountOfQuestions++) + '" value="';
        question = question + json.possibleAnswers[i] + '">' + json.possibleAnswers[i] + '<br> <i id="' + "questionCorrect" + (this.amountOfQuestions) + '"></i>';
    }
    return question;
}

function createFillIn(json) {
    var question = "<p>" + json.questionText + '</p> <input name="' + (this.amountOfQuestions++) + '" list="' + this.amountOfQuestions + '"/> <datalist id="' + this.amountOfQuestions + '">';
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option value="' + json.possibleAnswers[i] + '"/>';
    }
    question += '</datalist>';
    question += '<i id="' + "questionCorrect" + (this.amountOfQuestions) + '"></i>';

    return question;
}

function changeQuestions() {
    clearpage();

    var question = readJson("api/nextImageTask")
    for (var i = 0; i < question.length; i++) readQuestion(question.get(i));
}

function displayImageURL(imageURL) {
    return '<img class="imgCenter" src="' + imageURL + '">';
}

function pageDisplay() {
    var i;
    var imageTaskJSON = readJson("api/nextImageTask");

    var qArray = [imageTaskJSON.taskQuestions.length];

    //Displays the image on the page at the appropriate tag
    document.getElementById('image').innerHTML = displayImageURL(imageTaskJSON.taskQuestions[0].imageURL);

    //Displays the questions at the tags
    for (i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        qArray.push(imageTaskJSON.taskQuestions[i]);
        readQuestion(qArray[i]);
    }
}
