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
        this.canGiveNoAnswer = imageTaskSettings.canGiveNoAnswer;


        for(var i=0; i<this.questionAreaDisp.length; i++) {
            this.questionAreaDisp[i].addFollowupQuestions();
            document.getElementById("questionSet").appendChild(this.questionAreaDisp[i].element);
        }
    }

    submitAnswers(){
        for(var i=0; i<this.questionAreaDisp.length; i++){
            let current= this.questionAreaDisp[i].answerBox.checkCurrentResponse(this.response);
        }

        if(this.willDisplayFeedback) {
            this.giveFeedback(this.response.typesIncorrect);
        }

        let newResponse = {
            userId: this.userId,
            taskQuestionIds: this.response.taskQuestionIds,
            responseTexts: this.response.responseTexts
        };

        submitToAPI("api/recordResponse", newResponse);
    }

    nextQuestion(){
        location.reload();
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
