class ImageTaskResponse{
    constructor(userId, oldResponse, responseList){
        this.userId=userId;
        this.responseList=responseList;
        this.oldResponse=oldResponse;
        this.newResponse= new QuestionResponse(this.oldResponse.userId, this.oldResponse.taskQuestionIds);
    }
    addToResponseList() {
        this.responseList.push(this.newResponse);
        return this.responseList;
}

}