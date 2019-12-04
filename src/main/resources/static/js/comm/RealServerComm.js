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
            if (request.readyState === XMLHttpRequest.DONE && request.status === 200) {
                console.log("submitImageTaskResponse: success");
            } else if (request.readyState === XMLHttpRequest.DONE){
            console.log("submitImageTaskResponse: failure");
        }
        };
    }

    nextAuthorImageTask(studId, responseHandler){
        let request = new XMLHttpRequest();
        request.open("GET", "api/nextAuthorImageTask?userId=" + studId, false);
        request.send(null);
        let responseObject = JSON.parse(request.response);
        console.log("Loading new author image task:");
        console.log(responseObject);
        responseHandler.replaceImageTaskModel(responseObject);
    }

    submitAuthorImageTaskResponse(imageTaskToSubmit, responseHandler){
        let request = new XMLHttpRequest();
        request.open("POST", "api/submitAuthorImageTaskResponse");
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        console.log("Sending:");
        console.log(imageTaskToSubmit);
        request.send(JSON.stringify(imageTaskToSubmit));
        request.onreadystatechange = function () {
            if (request.readyState === XMLHttpRequest.DONE && request.status === 200) {
                console.log("submitAuthorImageTaskResponse: success");
                responseHandler.authorImageTaskSubmitted();
            } else if (request.readyState === XMLHttpRequest.DONE){
                console.log("submitAuthorImageTaskResponse: failure");
            }
        };
    }

    loadAllImageTasksFromModel(studId, responseHandler){
        let request = new XMLHttpRequest();
        request.open("GET", "api/authoredQuestions");
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === XMLHttpRequest.DONE && request.status === 200) {
                console.log("loadAllImageTasksFromModel: success");
                let responseObject = JSON.parse(request.response);
                console.log("Loading author review of image tasks:");
                console.log(responseObject);
                responseHandler.loadAllImageTasksFromModel(responseObject);
            } else if (request.readyState === XMLHttpRequest.DONE){
                console.log("loadAllImageTasksFromModel: failure");
            }
        };

    }

    transferAuthoredQuestionsToStudents(){
        let request = new XMLHttpRequest();
        request.open("GET", "/api/transferAuthoredQuestionsToStudents", false);
        request.send(null);
        console.log("transferAuthoredQuestionsToStudents message sent\n");
    }

}

function logCorrectAnswers(questionList, indent){
    for (let question of questionList){
        console.log(indent + question.questionText + ": " + question.correctAnswer);
        logCorrectAnswers(question.followupQuestions, indent+"    ");
    }
}