class ImageTaskDisplay {

    constructor(imageTaskJson, userId, imageTaskSettings, isAuthor) {
        this.userId = userId;
        this.response = new Response(userId);
        this.pageImage = new PageImage(imageTaskJson.imageUrl);
        this.questionAreaDisp = new buildQuestionAreas(imageTaskJson.taskQuestions, this.response);

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

        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            document.getElementById("questionSet").appendChild(this.questionAreaDisp[i].element);
        }
    }

    submitAnswers() {
        this.response.responseTexts= [];
        let canContinu;
        if (!this.isAuthor) {
            document.getElementById("errorFeedback").innerHTML = " ";
            this.checkAnswers();
            this.checkFollowUp();
            canContinu = this.checkIfCanContinu();
        } else {
            for (var i = 0; i < this.questionAreaDisp.length; i++) {
                this.questionAreaDisp[i].answerBox.recordCurrentResponse(this.response);
            }
            canContinu = true;
        }

        if (canContinu) {
            this.sendResponse();
        } else {
            document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    checkIfCanContinu(){
        if (!this.canGiveNoAnswer) {
            if (this.listOfCorrectAnswers.includes("")) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    sendResponse(){
        if (this.willDisplayFeedback) {
            this.giveFeedback(this.response.typesIncorrect);
        }

        this.submitResponse();

        if (!this.ableToResubmitAnswers) {
            document.getElementById("submitButton").classList.add("hide");
        }
        this.haveSubmited = true;
        document.getElementById("errorFeedback").innerHTML = "<font color=\"#663399\"> Response recorded</font>";
    }

    checkFollowUp(){
        if (this.haveSubmited) {
            for (var i = 0; i < this.questionAreaDisp.length; i++) {
                let current = this.questionAreaDisp[i];
                for (var x = 0; x < current.followUpAreas.length; x++) {
                    this.listOfCorrectAnswers.push(current.followUpAreas[x].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer));
                    if (!(this.response.taskQuestionIds.includes(current.followUpAreas[x].element.id))) {
                        this.response.addToQuestionIds(current.followUpAreas[x].element.id);
                    }
                }
            }
        }
    }

    checkAnswers() {
        this.listOfCorrectAnswers = [];
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];

            if (!(this.response.taskQuestionIds.includes(current.element.id))) {
                this.response.addToQuestionIds(current.element.id);
            }

            this.listOfCorrectAnswers.push(current.answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer));

            let correctness = this.listOfCorrectAnswers[this.listOfCorrectAnswers.length - 1];
            if (correctness === ResponseResult.correct) {
                this.questionAreaDisp[i].addFollowupQuestions();
            }
        }
    }

    submitResponse() {
        let newResponse = {
            userId: this.userId,
            taskQuestionIds: this.response.taskQuestionIds,
            responseTexts: this.response.responseTexts
        };

        console.log(this.response.taskQuestionIds);
        console.log(this.response.responseTexts);


        if (this.isAuthor) {
            //TODO: Needs a new URL
            submitToAPI("api/recordResponse", newResponse);
        } else {
            submitToAPI("api/recordResponse", newResponse);
        }
    }

    giveFeedback(typesSeenForFeedback) {
        if (typesSeenForFeedback.length > 0) {
            document.getElementById("helpfulFeedback").innerHTML = "Feedback: ";
        }
        for (var i = 0; i < typesSeenForFeedback.length; i++) {
            var type = typesSeenForFeedback[i];
            var response = this.feedbackByType[type];
            if (i < typesSeenForFeedback.length - 1) {
                response += ", ";
            }
            document.getElementById("helpfulFeedback").innerHTML += response;
        }
    }

}
