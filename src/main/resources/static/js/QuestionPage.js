var amountOfQuestions = 0;
var QuestionAnswers = [];
var UserID= null;
var responsesGivenText = [];
var QuestionIDs = [];


var UnsureShowsCorrectAnswer;
var FeedbackByType;
var ableToResumbitAnswers;
var ScoreType;
var ShowScore;

function nextQuestionSet(){
    clearPage();
    canvasApp();
}

function checkAnswers(){
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    checkAndRecordAnswers();
    //call CreateResponse to send answers back to the server
    createResponses();
    clearQuestionAnswers();
}


function createResponses(){
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
}