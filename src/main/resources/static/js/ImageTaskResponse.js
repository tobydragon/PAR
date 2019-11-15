class ImageTaskResponse{
    constructor(userId, responseList){
        this.userId=userId;
        this.responseList=responseList;
        if(responseList.length===1){
            this.oldResponse=responseList[0];
        }
        this.newResponse= null;
    }
    addToResponseList() {
        this.newResponse= new QuestionResponse(this.oldResponse.taskQuestionIds,this.oldResponse.responseTexts,this.oldResponse.questionText);
        this.responseList.push(this.newResponse);
        return this.responseList;
}

}