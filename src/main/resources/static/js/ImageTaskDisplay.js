class ImageTaskDisplay {

    constructor(imageTaskJson, userId, imageTaskSettings, isAuthor, canvasName, pageDisplaySettings) {
        this.userId = userId;
        this.response = new Response(userId);
        this.hasFolloup= false;
        //settings
        this.unsureShowsCorrectAnswer = imageTaskSettings.unsureShowsCorrectAnswer;
        this.feedbackByType = imageTaskSettings.feedbackByType;
        this.willDisplayFeedback = imageTaskSettings.willDisplayFeedback;
        this.ableToResubmitAnswers = imageTaskSettings.ableToResubmitAnswers;
        this.mustSubmitAnswersToContinue = imageTaskSettings.mustSubmitAnswersToContinue;
        this.haveSubmited = 0;
        this.canGiveNoAnswer = imageTaskSettings.canGiveNoAnswer;
        this.pageSettings= pageDisplaySettings;

        this.listOfCorrectAnswers = [];
        this.isAuthor = isAuthor;

        if (imageTaskJson.imageUrl === "noMoreQuestions") {
            this.createCanvas("../images/ParLogo.png", canvasName);
        } else {
            this.createCanvas(imageTaskJson.imageUrl, canvasName);
        }
        this.displayImageUrl(imageTaskJson.imageUrl);
        this.questionAreaDisp = new buildQuestionAreas(imageTaskJson.taskQuestions, this.response);

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
            this.authorSubmitResponses();
            canContinu = true;
        }

        if (canContinu) {
            this.displayFeedback();
            sendResponse(this.response, this.ableToResubmitAnswers, this.isAuthor, this.pageSettings, this.hasFolloup, this.haveSubmited);
            this.haveSubmited +=1;
            this.hasFolloup=false;
        } else {
            document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    authorSubmitResponses() {
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            let value= current.answerBox.recordCurrentResponse(this.response);
            if(value!==ResponseResult.blank) {
                addToResponseIds(this.response, current.element.id);
            }
            for (var x = 0; x < current.followUpAreas.length; x++) {
                value= current.followUpAreas[x].answerBox.recordCurrentResponse(this.response);
                if(value!==ResponseResult.blank) {
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
            let correctness=current.followUpAreas[x].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer, current.followUpAreas[x].element.id);
            this.listOfCorrectAnswers.push(correctness);
        }
    }

    checkAnswers() {
        this.response.taskQuestionIds= [];
        this.listOfCorrectAnswers = [];
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            let correctness = current.answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer, current.element.id);
            this.listOfCorrectAnswers.push(correctness);
            if (checkIfShouldAddFollowupQ(correctness)) {
                current.addFollowupQuestions();
                if(current.followUpAreas.length>0){
                    this.hasFolloup= true;
                }
            }
            this.checkFollowUp(current);
        }
    }

    createCanvas(imageUrl, name){
        let newCanvas = document.createElement("CANVAS");
        newCanvas.id= name;
        newCanvas.width= "1024";
        newCanvas.height= "768";
        //newCanvas.classList.add("center-block");
        newCanvas.classList.add("canvas");
        document.getElementById("canvasArea").appendChild(newCanvas);
        this.pageImage = new PageImage(imageUrl, name);
    }

    lockInCorrectAnswers(){
        for(var i=0; i<this.questionAreaDisp.length; i++){
            let current= this.questionAreaDisp[i];
            current.answerBox.inputTextbox.value= current.answerBox.correctResponse;
            disableElement(current.answerBox.inputTextbox);
            for(var x=0; x<current.followUpAreas.length; x++){
                current.followUpAreas[x].answerBox.inputTextbox.value= current.followUpAreas[x].answerBox.correctResponse;
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

    console.log(newResponse.taskQuestionIds);
    console.log(newResponse.responseTexts);

    if (isAuthor) {
        submitToAPI("api/submitAuthorImageTaskResponse", newResponse,pageSettings.showScore ,pageSettings.scoreType ,this.userID );
    } else {
        submitToAPI("api/recordResponse", newResponse, pageSettings.showScore ,pageSettings.scoreType ,this.userID);
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

function sendResponse(response, ableToResubmitAnswers, isAuthor, pageSettings, hasFollowup, timesSubmitted) {
    submitResponse(response, isAuthor, pageSettings);

    if (!ableToResubmitAnswers) {
        if(hasFollowup){
            if(timesSubmitted>0){
                document.getElementById("submitButton").classList.add("hide");
            }
        }else {
            document.getElementById("submitButton").classList.add("hide");
        }
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
