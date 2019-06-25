var responsesGivenText = [];
var QuestionIDs = [];
var UserID;

function getUserIdfromUser() {
    UserID = document.getElementById("inputId").value;
    document.getElementById("userID").innerText = UserID;
    changeUserID(UserID);
}

function setVariables() {
    responsesGivenText = getResponsesText();
    QuestionIDs = getQuestionIDs();
    UserID = sendUserId();
}

function createResponseJson() {
    var newResponse;
    if (UserID != null) {
        newResponse = {
            userId: UserID,
            taskQuestionIds: QuestionIDs,
            responseTexts: responsesGivenText
        };
        console.log(QuestionIDs);
        console.log(responsesGivenText);
    } else {
        newResponse = {
            userId: "Student",
            taskQuestionIds: QuestionIDs,
            responseTexts: responsesGivenText
        };
    }
    return newResponse;
}

function submitToAPI(url, objectToSubmit) {
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(objectToSubmit));
    request.onreadystatechange = function () {
        if (request.status === 200) {
            setCurrentScore();
        } else {
            window.location.replace("/error");
        }
    };
}

function generateResponseJSON() {
    setVariables();
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
    console.log("Response Sent to "+ UserID);

}


//for testing purposes only
function testSetVariables() {
    responsesGivenText = ["Lateral", "ligament", "Unsure"];
    QuestionIDs = ["PlaneQ1", "StructureQ1", "ZoneQ1"];
    UserID = "Hewwo123";
}

function testGenerateReponseJSON() {
    testSetVariables();
    return createResponseJson();
}
