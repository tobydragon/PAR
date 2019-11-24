class RealServerComm{

    constructor(){}

    nextStudentImageTask(studId, responseHandler){
        let request = new XMLHttpRequest();
        request.open("GET", "api/nextImageTask?userId=" + studId, false);
        request.send(null);
        let responseObject = JSON.parse(request.response);
        console.log("Loading new student image task:");
        console.log(responseObject);
        logCorrectAnswers(responseObject.taskQuestions, "");
        responseHandler.replaceImageTaskModel(responseObject);
    }

    submitImageTaskResponse(imageTaskToSubmit){
        let request = new XMLHttpRequest();
        request.open("POST", "api/recordResponse");
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        request.send(JSON.stringify(imageTaskToSubmit));
        request.onreadystatechange = function () {
            if (request.status === 200) {
                console.log("submitImageTaskResponse: success");
            } else {
               console.log("submitImageTaskResponse: failure");
            }
        };
    }
}

function logCorrectAnswers(questionList, indent){
    for (let question of questionList){
        console.log(indent + question.questionText + ": " + question.correctAnswer);
        logCorrectAnswers(question.followupQuestions, indent+"    ");
    }
}