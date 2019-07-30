class ImageTaskDisplay {

    constructor(imageTaskJson, userId, imageTaskSettings, isAuthor, canvasName, pageDisplaySettings) {
        this.userId = userId;
        this.response = new Response(userId);
        this.imageUrl = imageTaskJson.imageUrl;
        this.imageTaskJson = imageTaskJson;
        this.taskQuestions = imageTaskJson.taskQuestions;
        //settings
        this.unsureShowsCorrectAnswer = imageTaskSettings.unsureShowsCorrectAnswer;
        this.feedbackByType = imageTaskSettings.feedbackByType;
        this.willDisplayFeedback = imageTaskSettings.willDisplayFeedback;
        this.ableToResubmitAnswers = imageTaskSettings.ableToResubmitAnswers;
        this.mustSubmitAnswersToContinue = imageTaskSettings.mustSubmitAnswersToContinue;
        this.haveSubmited = false;
        this.canGiveNoAnswer = imageTaskSettings.canGiveNoAnswer;
        this.pageSettings = pageDisplaySettings;
        this.canvasName = canvasName;
        this.listOfCorrectAnswers = [];
        this.isAuthor = isAuthor;

        //buildQuestionAreasAuthor(this.isAuthor)
        if (!isAuthor) {
            addUnsureToAnswers(imageTaskJson.taskQuestions);
        }
    }

    displayImageUrl(url) {
        document.getElementById("Ids").innerText = url;
    }

    submitAnswers() {
        this.response.responseTexts = [];
        let canContinu;
        document.getElementById("errorFeedback").innerHTML = " ";

        if (!this.isAuthor) {
            this.checkAnswers();
            canContinu = checkIfCanContinu(this.canGiveNoAnswer, this.listOfCorrectAnswers);
        } else {
            console.log("author Submit response");
            this.authorSubmitResponses();
            canContinu = true;
        }

        if (canContinu) {
            this.displayFeedback();
            this.haveSubmited = sendResponse(this.response, this.ableToResubmitAnswers, this.isAuthor, this.pageSettings);
        } else {
            document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    authorSubmitResponses() {
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            let value = current.answerBox.recordCurrentResponse(this.response);
            if (value !== ResponseResult.blank) {
                addToResponseIds(this.response, current.element.id);
            }
            for (var x = 0; x < current.followUpAreas.length; x++) {
                value = current.followUpAreas[x].answerBox.recordCurrentResponse(this.response);
                if (value !== ResponseResult.blank) {
                    addToResponseIds(this.response, current.followUpAreas[x].element.id);
                }
            }
        }
    }

    displayFeedback() {
        if (this.willDisplayFeedback) {
            document.getElementById("helpfulFeedback").innerHTML = giveFeedback(this.response.typesIncorrect, this.feedbackByType);
        }
    }

    checkFollowUp(current) {
        for (var x = 0; x < current.followUpAreas.length; x++) {
            let correctness = current.followUpAreas[x].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer);
            this.listOfCorrectAnswers.push(correctness);
            if (correctness !== ResponseResult.blank) {
                addToResponseIds(this.response, current.followUpAreas[x].element.id);
            }
        }
    }

    checkAnswers() {
        this.listOfCorrectAnswers = [];
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            let correctness = current.answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer);
            if (correctness !== ResponseResult.blank) {
                addToResponseIds(this.response, current.element.id);
            }
            this.listOfCorrectAnswers.push(correctness);
            if (checkIfShouldAddFollowupQ(correctness)) {
                current.addFollowupQuestions();
            }
            if (this.haveSubmited) {
                this.checkFollowUp(current);
            }
        }
    }



    createQuestionAreaElement() {
        let questionElement = document.createElement('div');
        questionElement.classList.add('col-4');
        this.questionAreaDisp = new buildQuestionAreas(this.imageTaskJson.taskQuestions, this.response);
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            if (this.isAuthor) {
                this.questionAreaDisp[i].addFollowupQuestions();
            }
            questionElement.appendChild(this.questionAreaDisp[i].element);
        }
        return questionElement;
    }

    createCanvasElement() {
        let canvasElement = document.createElement('div');
        canvasElement.classList.add('col-6');
        canvasElement.classList.add('imgCenter');
        let canvas;
        if (this.imageUrl === "noMoreQuestions") {
            canvas = new PageImage("../images/ParLogo.png", this.canvasName);

        } else {
            canvas = new PageImage(this.imageUrl, this.canvasName);
        }
        this.displayImageUrl(this.imageUrl);
        canvasElement.appendChild(canvas.element);
        canvas.loadImage();
        return canvasElement;
    }

    createImageTaskElement() {
        let outerImageTaskNode = document.createElement('div');
        outerImageTaskNode.classList.add('row');

        let canvasNode = this.createCanvasElement();
        let questionAreaNode = this.createQuestionAreaElement();
        let spaceNode0 = document.createElement('div');
        spaceNode0.classList.add('col-1');
        let spaceNode1 = document.createElement('div');
        spaceNode1.classList.add('col-1');
        outerImageTaskNode.appendChild(spaceNode0);
        outerImageTaskNode.appendChild(canvasNode);
        outerImageTaskNode.appendChild(questionAreaNode);
        outerImageTaskNode.appendChild(spaceNode1);
        return outerImageTaskNode;
    }

    lockInCorrectAnswers() {
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            current.answerBox.inputTextbox.value = current.answerBox.correctResponse;
            disableElement(current.answerBox.inputTextbox);
            for (var x = 0; x < current.followUpAreas.length; x++) {
                current.followUpAreas[x].answerBox.inputTextbox.value = current.followUpAreas[x].answerBox.correctResponse;
                disableElement(current.followUpAreas[x].answerBox.inputTextbox);
            }
        }
    }

}

function addUnsureToAnswers(questionObjectList) {
    let questionAreaList = [];
    for (let questionObject of questionObjectList) {
        questionObject.possibleAnswers.push(ResponseResult.unsure);
        addUnsureToAnswers(questionObject.followupQuestions);
    }
}

function checkIfShouldAddFollowupQ(correctness) {
    if (correctness === ResponseResult.correct) {
        return true;
    } else {
        return false;
    }
}

function addToResponseIds(response, id) {
    if (!(response.taskQuestionIds.includes(id))) {
        response.addToQuestionIds(id);
    }
}

function submitResponse(response, isAuthor, pageSettings) {
    let newResponse = {
        userId: response.userId,
        taskQuestionIds: response.taskQuestionIds,
        responseTexts: response.responseTexts
    };

    if (isAuthor) {
        submitToAPI("api/submitAuthorImageTaskResponse", newResponse, pageSettings.showScore, pageSettings.scoreType, this.userID);
    } else {
        submitToAPI("api/recordResponse", newResponse, pageSettings.showScore, pageSettings.scoreType, this.userID);
    }
}

function giveFeedback(typesSeenForFeedback, feedbackByType) {
    var feedbackString = "";
    if (typesSeenForFeedback.length > 0) {
        feedbackString = "Feedback: ";
    }
    for (var i = 0; i < typesSeenForFeedback.length; i++) {
        var type = typesSeenForFeedback[i];
        var response = feedbackByType[type];
        if (i < typesSeenForFeedback.length - 1) {
            response += ", ";
        }
        feedbackString += response;
    }

    return feedbackString;
}

function sendResponse(response, ableToResubmitAnswers, isAuthor, pageSettings) {
    submitResponse(response, isAuthor, pageSettings);

    if (!ableToResubmitAnswers) {
        document.getElementById("submitButton").classList.add("hide");
    }
    document.getElementById("errorFeedback").innerHTML = "<font color=\"#663399\"> Response recorded</font>";

    return true;
}

function checkIfCanContinu(canGiveNoAnswer, listOfCorrectAnswers) {
    if (!canGiveNoAnswer) {
        if (listOfCorrectAnswers.includes("")) {
            return false;
        } else {
            return true;
        }
    } else {
        return true;
    }
}
