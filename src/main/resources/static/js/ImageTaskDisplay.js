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
            this.questionAreaDisp[i].addFollowupQuestions();
            document.getElementById("questionSet").appendChild(this.questionAreaDisp[i].element);
        }
    }

    submitAnswers() {
        let canContinu;
        if (!this.isAuthor) {
            document.getElementById("errorFeedback").innerHTML = " ";
            canContinu = this.checkAnswers();
        } else {
            for (var i = 0; i < this.questionAreaDisp.length; i++) {
                this.questionAreaDisp[i].answerBox.recordCurrentResponse(this.response);
            }
            canContinu = true;
        }

        if (canContinu) {
            if (this.willDisplayFeedback) {
                this.giveFeedback(this.response.typesIncorrect);
            }

            this.submitResponse();

            if (!this.ableToResubmitAnswers) {
                document.getElementById("submitButton").classList.add("hide");
            }
            this.haveSubmited = true;
            document.getElementById("errorFeedback").innerHTML = "<font color=\"#663399\"> Response recorded</font>";
        } else {
            document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    checkAnswers() {
        this.listOfCorrectAnswers = [];
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            if (!(this.response.taskQuestionIds.includes(this.questionAreaDisp[i].element.id))) {
                this.response.addToQuestionIds(this.questionAreaDisp[i].element.id);
            }

            this.listOfCorrectAnswers.push(this.questionAreaDisp[i].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer, this.questionAreaDisp[i].element.id));

            let correctness = this.listOfCorrectAnswers[this.listOfCorrectAnswers.length - 1];
            if (correctness === "correct") {
                this.questionAreaDisp[i].followup.classList.remove("hide");
            }
        }
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

    submitResponse() {
        let newResponse = {
            userId: this.userId,
            taskQuestionIds: this.response.taskQuestionIds,
            responseTexts: this.response.responseTexts
        };


        if (this.isAuthor) {
            //TODO: Needs a new URL
            submitToAPI("api/recordResponse", newResponse);
        } else {
            submitToAPI("api/recordResponse", newResponse);
        }
    }

    nextQuestion() {
        if (!this.mustSubmitAnswersToContinue) {
            location.reload();
        } else {
            if (this.haveSubmited) {
                location.reload();
            } else {
                document.getElementById("errorFeedback").innerHTML = "<font color=red>Must submit answers to continue</font>";
            }
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
