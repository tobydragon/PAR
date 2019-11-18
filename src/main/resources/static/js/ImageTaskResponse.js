class ImageTaskResponse{
    constructor(oldResponse){
        this.userId=oldResponse.userId;
        this.questionResponses = [];
        for (let i=0; i<oldResponse.questionIds.length; i++){
            this.questionResponses.push(new QuestionResponse(oldResponse.questionIds[i], oldResponse.responseTexts[i]), oldResponse.questionTexts[i]);
        }
    }
}