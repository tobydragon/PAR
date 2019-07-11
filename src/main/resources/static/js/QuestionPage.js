var amountOfQuestions = 0;
var questionAnswers = [];
var userID = null;
var responsesGivenText = [];
var questionIDs = [];
var questionTypes = [];
var typesSeenForFeedback = [];

var unsureShowsCorrectAnswer;
var feedbackByType;
var ableToResubmitAnswers;
var scoreType;
var showScore;
var mustSubmitAnswersToContinue;
var numberOfQuestionsAnswered = 0;
var canGiveNoAnswer;
var willDisplayFeedback;


//
//Passing and Clearing variables
//
function sendUserId() {
    if (userID != null) {
        return userID;
    } else {
        return "Student";
    }
}

//Clears question IDs from working image task.
function clearQuestionIDs() {
    questionIDs = [];
}
//Removes all text from the screen at id questionSet to prep for next image task.
function clearPage() {
    document.getElementById('questionSet').innerHTML = " ";
    amountOfQuestions = 0;
}

//Clears the answers from the page.
function clearQuestionAnswers() {
    questionAnswers = [];
}

function clearResponses() {
    responsesGivenText = [];
}

function logout() {
    userID = null;
    return location.replace('/login');
}

function getSettings() {
    try {
        var settings = readJson("api/getSettings");

    } catch (Exception) {
        window.onerror = function (msg) {
            location.replace('/error?message=' + msg);
        }
    }

    unsureShowsCorrectAnswer = settings.unsureShowsCorrectAnswer;
    feedbackByType = settings.feedbackByType;
    ableToResubmitAnswers = settings.ableToResubmitAnswers;
    scoreType = settings.scoreType;
    showScore = settings.showScore;
    mustSubmitAnswersToContinue = settings.mustSubmitAnswersToContinue;
    canGiveNoAnswer = settings.canGiveNoAnswer;
    willDisplayFeedback = settings.willDisplayFeedback;
}

function addToTypesSeenForFeedback(type) {
    if (!typesSeenForFeedback.includes(type)) {
        typesSeenForFeedback.push(type);
    }
}


//
//Create Page
//

//generates question for html based on the question given (the JSON)
function generateQuestion(question) {
    var difficultyStr = createDatalistDropdown(question, amountOfQuestions);
    amountOfQuestions++;

    questionAnswers.push(question.correctAnswer);
    questionIDs.push(question.id);
    questionTypes.push(question.type);
    displayQuestion(difficultyStr);
}

function checkAnswers() {
    console.log(questionAnswers);
    document.getElementById("errorFeedback").innerHTML = " ";
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    displayCheckAndRecordAnswers();
    //call CreateResponse to send answers back to the server
    numberOfQuestionsAnswered = responsesGivenText.length;
    //if (numberOfQuestionsAnswered == amountOfQuestions) {
    createResponses();
    //} else {
    //    document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
    //    clearFeedback();
    //    clearQuestionCorrectnessResponses();
    //}
    if (!ableToResubmitAnswers) {
        disableSubmit();
    }
    if (!canGiveNoAnswer && numberOfQuestionsAnswered != amountOfQuestions) {
        reEnableSubmit();
    }
    clearResponses();
}

//Checks the answers given in the questions against the record of what is correct/incorrect.
function displayCheckAndRecordAnswers() {
    var form = document.getElementById("questionSetForm");
    for (var i = 0; i < amountOfQuestions; i++) {
        var currentName = "q" + i;
        var currentAnswer = form[currentName].value;
        currentAnswer = currentAnswer.toLowerCase();

        responsesGivenText.push(currentAnswer);

        var isCorrect;
        if (currentAnswer == "unsure") {
            console.log(currentAnswer);
            disableField('optionUnsure', currentName);
        }
        if (currentAnswer == questionAnswers[i]) {
            isCorrect = "Correct";
        } else if (currentAnswer == "") {
            responsesGivenText.pop();
            isCorrect = "blank";
        } else if (currentAnswer != questionAnswers[i]) {
            if (currentAnswer == "unsure") {
                isCorrect = "unsure";
            } else {
                isCorrect = "Incorrect";
            }
            addToTypesSeenForFeedback(questionTypes[i]);
        }
        var displayAreaName = "questionCorrect" + i;
        document.getElementById(displayAreaName).innerHTML = displayCheck(isCorrect, questionAnswers[i], unsureShowsCorrectAnswer);
    }
    if (willDisplayFeedback) {
        generateFeedback();
    }
}

function nextQuestionSet() {
    typesSeenForFeedback = [];
    clearQuestionAnswers();
    var canContinue = true;
    if (mustSubmitAnswersToContinue) {
        canContinue = checkForAnswers();
    }
    if (canContinue) {
        clearPage();
        clearFeedback();
        canvasApp();
        if (!ableToResubmitAnswers) {
            reEnableSubmit();
        }
        document.getElementById("errorFeedback").innerHTML = " ";
    }
}
