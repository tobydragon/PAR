var amountOfQuestions = 0;
var QuestionAnswers = [];
var UserID= null;
var responsesGivenText = [];
var QuestionIDs = [];
var questionTypes = [];
var typesSeenForFeedback= [];

var unsureShowsCorrectAnswer;
var feedbackByType;
var ableToResubmitAnswers;
var scoreType;
var showScore;

function nextQuestionSet(){
    clearPage();
    clearFeedback();
    canvasApp();
    if(!ableToResubmitAnswers){
        reEnableSubmit();
    }
}

function checkAnswers() {
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    checkAndRecordAnswers();
    //call CreateResponse to send answers back to the server
    createResponses();
    clearQuestionAnswers();
    if (!ableToResubmitAnswers) {
        disableSubmit();
    }
}


function createResponses(){
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
}