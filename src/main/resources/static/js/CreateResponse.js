var responsesGivenText= [];
var responsesGivenCorrectness= [];

var QuestionIDs= [];

function setVariables(){
    responsesGivenText= getResponsesText();
    responsesGivenCorrectness= getResponsesCorrectness();
    QuestionIDs= getQuestionIDs();
}

function creasteResponseJson(){

}


function generateResponseJSON(){
    setVariables();
    var object= createResponseJson();
}