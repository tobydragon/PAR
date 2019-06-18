var responsesGivenText= [];
var QuestionIDs= [];
var UserID;

function setVariables(){
    responsesGivenText= getResponsesText();
    QuestionIDs= getQuestionIDs();
    UserID= getQuestionIDs();
}

function createResponseJson(){
    var newResponse= {
        userId: UserID,
        taskQuestionIds: QuestionIDs,
        responseTexts: responsesGivenText
    };
    return newResponse
}


function generateResponseJSON(){
    setVariables();
    var object= createResponseJson();
}