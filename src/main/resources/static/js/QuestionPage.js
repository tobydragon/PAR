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
var willDisplayFeedback;


function nextQuestionSet(){
    typesSeenForFeedback=[];
    clearQuestionAnswers();
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
    console.log(questionAnswers);
    document.getElementById("errorFeedback").innerHTML =" ";
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    checkAndRecordAnswers();
    //call CreateResponse to send answers back to the server
    numberOfQuestionsAnswered= responsesGivenText.length;
    if(numberOfQuestionsAnswered==amountOfQuestions) {
        createResponses();
    } else {
        document.getElementById("errorFeedback").innerHTML ="<font color=red>No response was recorded because you did not answer all the questions</font>";
        clearFeedback();
        clearQuestionCorrectnessResponses();
    }
    if (!ableToResubmitAnswers ) {
        disableSubmit();
    }
    if(!canGiveNoAnswer && numberOfQuestionsAnswered!=amountOfQuestions){
        reEnableSubmit();
    }
    clearResponses();
}


function createResponses(){
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
}
