class ImageTaskDisplay{

    constructor(imageTaskJson, userId, imageTaskSettings){
        this.userId=userId;
        this.response= new Response(userId);
        this.pageImage= new PageImage(imageTaskJson.imageUrl);
        this.questionAreaDisp= new buildQuestionAreas(imageTaskJson.taskQuestions, this.response);

        //settings
        this.unsureShowsCorrectAnswer = imageTaskSettings.unsureShowsCorrectAnswer;

        this.feedbackByType = imageTaskSettings.feedbackByType;
        this.willDisplayFeedback = imageTaskSettings.willDisplayFeedback;

        this.ableToResubmitAnswers = imageTaskSettings.ableToResubmitAnswers;
        this.mustSubmitAnswersToContinue = imageTaskSettings.mustSubmitAnswersToContinue;
        this.haveSubmited=false;
        this.canGiveNoAnswer = imageTaskSettings.canGiveNoAnswer;

        this.listOfCorrectAnswers= [];

        for(var i=0; i<this.questionAreaDisp.length; i++) {
            this.questionAreaDisp[i].addFollowupQuestions();
            document.getElementById("questionSet").appendChild(this.questionAreaDisp[i].element);
        }
    }

    submitAnswers(){
        document.getElementById("errorFeedback").innerHTML= " ";
        let canContinu= this.checkAnswers();

        if(canContinu) {
            if (this.willDisplayFeedback) {
                this.giveFeedback(this.response.typesIncorrect);
            }

            this.submitResponse();

            if (!this.ableToResubmitAnswers) {
                document.getElementById("submitButton").classList.add("hide");
            }
            this.haveSubmited = true;
            document.getElementById("errorFeedback").innerHTML= "<font color=\"#663399\"> Response recorded</font>";
        } else {
            document.getElementById("errorFeedback").innerHTML= "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    checkAnswers(){
        this.listOfCorrectAnswers= [];
        for(var i=0; i<this.questionAreaDisp.length; i++){
            this.listOfCorrectAnswers.push(this.questionAreaDisp[i].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer));
        }
        if(!this.canGiveNoAnswer){
            if(this.listOfCorrectAnswers.includes("")){
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    submitResponse(){
        let newResponse = {
            userId: this.userId,
            taskQuestionIds: this.response.taskQuestionIds,
            responseTexts: this.response.responseTexts
        };

        submitToAPI("api/recordResponse", newResponse);
    }

    nextQuestion(){
        if(!this.mustSubmitAnswersToContinue){
            location.reload();
        } else {
            if(this.haveSubmited){
                location.reload();
            } else {
                document.getElementById("errorFeedback").innerHTML= "<font color=red>Must submit answers to continue</font>";
            }
        }
    }

    giveFeedback(typesSeenForFeedback){
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
