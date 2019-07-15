class ImageTaskDisplay{

    constructor(imageTaskJson, userId){
        this.userId=userId;
        this.response= new Response(userId);
        this.pageImage= new PageImage(imageTaskJson.imageUrl);
        this.questionAreaDisp= new buildQuestionAreas(imageTaskJson.taskQuestions, this.response);

        for(var i=0; i<this.questionAreaDisp.length; i++) {
            this.questionAreaDisp[i].addFollowupQuestions();
            document.getElementById("questionSet").appendChild(this.questionAreaDisp[i].element);
        }
    }

    submitAnswers(){
        for(var i=0; i<this.questionAreaDisp.length; i++){
            this.questionAreaDisp[i].answerBox.checkCurrentResponse(this.response);
        }

        submitToAPI("api/recordResponse", this.response);
    }
}
