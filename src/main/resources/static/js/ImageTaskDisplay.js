class ImageTaskDisplay {

    constructor(imageTaskJson, userId, imageTaskSettings, isAuthor) {
        this.userId = userId;
        this.response = new Response(userId);
        //settings
        this.unsureShowsCorrectAnswer = imageTaskSettings.unsureShowsCorrectAnswer;
        this.feedbackByType = imageTaskSettings.feedbackByType;
        this.willDisplayFeedback = imageTaskSettings.willDisplayFeedback;
        this.ableToResubmitAnswers = imageTaskSettings.ableToResubmitAnswers;
        this.mustSubmitAnswersToContinue = imageTaskSettings.mustSubmitAnswersToContinue;
        this.haveSubmited = false;
        this.canGiveNoAnswer = imageTaskSettings.canGiveNoAnswer;

        this.listOfCorrectAnswers = [];
        this.isAuthor = isAuthor;

        if (imageTaskJson.imageUrl === "NoMoreQuestions") {
            //TODO
        } else {
            this.pageImage = new PageImage(imageTaskJson.imageUrl);
            this.displayImageUrl(imageTaskJson.imageUrl);
        }
        //buildQuestionAreasAuthor(this.isAuthor)
        if (!isAuthor) {
            addUnsureToAnswers(imageTaskJson.taskQuestions);
        }

        this.questionAreaDisp = new buildQuestionAreas(imageTaskJson.taskQuestions, this.response);

        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            if (isAuthor) {
                this.questionAreaDisp[i].addFollowupQuestions();
            }
            document.getElementById("questionSet").appendChild(this.questionAreaDisp[i].element);
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
            this.haveSubmited = sendResponse(this.response, this.ableToResubmitAnswers, this.isAuthor);
        } else {
            document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    authorSubmitResponses() {
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            current.answerBox.recordCurrentResponse(this.response);
            addToResponseIds(this.response, current.element.id);
            for (var x = 0; x < current.followUpAreas.length; x++) {
                addToResponseIds(this.response, current.followUpAreas[x].element.id);
                current.followUpAreas[x].answerBox.recordCurrentResponse(this.response);
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
            this.listOfCorrectAnswers.push(current.followUpAreas[x].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer));
            addToResponseIds(this.response, current.followUpAreas[x].element.id);
        }
    }

    checkAnswers() {
        this.listOfCorrectAnswers = [];
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            addToResponseIds(this.response, current.element.id);
            let correctness = current.answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer);
            this.listOfCorrectAnswers.push(correctness);
            if (checkIfShouldAddFollowupQ(correctness)) {
                current.addFollowupQuestions();
            }
            if (this.haveSubmited) {
                this.checkFollowUp(current);
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

function submitResponse(response, isAuthor) {
    let newResponse = {
        userId: response.userId,
        taskQuestionIds: response.taskQuestionIds,
        responseTexts: response.responseTexts
    };

    if (isAuthor) {
        console.log(newResponse.responseTexts);
        console.log(newResponse.taskQuestionIds);
        submitToAPI("api/submitAuthorImageTaskResponse", newResponse);
    } else {
        submitToAPI("api/recordResponse", newResponse);
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

function sendResponse(response, ableToResubmitAnswers, isAuthor) {
    submitResponse(response, isAuthor);

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
