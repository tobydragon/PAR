var amountOfQuestions = 0;
var QuestionAnswers = [];
var UserID= null;
var responsesGivenText = [];
var QuestionIDs = [];

window.addEventListener("load", canvasApp, false);

function canvasSupport() {
    return Modernizr.canvas;
}

function nextQuestionSet(){
    canvasApp();
}

function CheckAnswers(){
    //check and report to the user what they got right or wrong as well as add to list responsesGivenText
    checkAndRecordAnswers();
    //call CreateResponse to send answers back to the server
    generateResponseJSON();
    clearQuestionAnswers();
}

function CreateResponses(){
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
}