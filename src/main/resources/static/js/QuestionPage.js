var amountOfQuestions = 0;
var questionAnswers = [];
var userID = null;
var responsesGivenText = [];
var questionIDs = [];
var questionTypes = [];
var typesSeenForFeedback = [];
var questionObjects = [];

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
    var questionStr = createDatalistDropdown(question, amountOfQuestions);
    amountOfQuestions++;
    questionObjects.push(question);
    questionAnswers.push(question.correctAnswer);
    questionIDs.push(question.id);
    questionTypes.push(question.type);
    displayQuestion(questionStr);
}

function generateFollowupQuestions(question) {
    if (question.hasOwnProperty("followupQuestions")) {
        var questionStrFollowup = createDatalistDropdown(question, amountOfQuestions);
        amountOfQuestions++;
        displayQuestion(questionStrFollowup);
    }
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

        responsesGivenText.push(currentAnswer);

        var answerEvaluationString;
        answerEvaluationString = compareAnswers(questionAnswers[i], currentAnswer, questionTypes[i]);
        disableFields(answerEvaluationString, currentName);
        if (answerEvaluationString === "Correct") {
            let followupString = generateFollowupQuestions(questionObjects[i]);
            displayQuestion(followupString);
        }
        console.log(questionTypes);
        console.log(responsesGivenText);

        var displayAreaName = "questionCorrect" + i;
        displayAnswers(displayAreaName, answerEvaluationString, questionAnswers[i], unsureShowsCorrectAnswer);
    }
    if (willDisplayFeedback) {
        generateFeedback();
    }
}

function disableFields(correctAnswerValue, elementToDisable) {
    if (correctAnswerValue === "Correct") {
        disableField(elementToDisable);
    } else if (correctAnswerValue === "Unsure") {
        disableField(elementToDisable);
    }
}

function compareAnswers(correctAnswer, userAnswer, questionType) {
    var answerEvaluationString = "";
    correctAnswer = correctAnswer.toLowerCase();
    userAnswer = userAnswer.toLowerCase();
    if (userAnswer === correctAnswer) {
        return answerEvaluationString = "Correct";
    } else if (userAnswer != correctAnswer) {
        if (userAnswer === "unsure") {
            return answerEvaluationString = "Unsure";
        } else if (userAnswer === "") {
            responsesGivenText.pop();
            return answerEvaluationString = "blank";
        }
        addToTypesSeenForFeedback(questionType);
        return answerEvaluationString = "Incorrect";
    }
}

function displayAnswers(elementIdToDisplay, answerEvaluationString, questionAnswer, unsureShowsCorrectAnswer) {
    document.getElementById(elementIdToDisplay).innerHTML = displayCheck(answerEvaluationString, questionAnswer, unsureShowsCorrectAnswer);
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
