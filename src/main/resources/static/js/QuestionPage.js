var amountOfQuestions = 0;
var questionAnswers = [];
var userID= null;
var responsesGivenText = [];
var questionIDs = [];
var questionTypes = [];
var typesSeenForFeedback= [];

var unsureShowsCorrectAnswer;
var feedbackByType;
var ableToResubmitAnswers;
var scoreType;
var showScore;
var mustSubmitAnswersToContinue;
var numberOfQuestionsAnswered=0;
var canGiveNoAnswer;

function nextQuestionSet(){
    var canContinue= true;
    if(mustSubmitAnswersToContinue){
        canContinue= checkForAnswers();
    }
    if(canContinue) {
        clearPage();
        clearFeedback();
        canvasApp();
        if (!ableToResubmitAnswers) {
            reEnableSubmit();
        }
        document.getElementById("errorFeedback").innerHTML =" ";
    }
}

function checkAnswers() {
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    checkAndRecordAnswers();
    //call CreateResponse to send answers back to the server

    createResponses();

    numberOfQuestionsAnswered= responsesGivenText.length;

    clearQuestionAnswers();
    if (!ableToResubmitAnswers) {
        disableSubmit();
    }
}


function createResponses(){
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
}
