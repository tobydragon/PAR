class Response{

    constructor(userID){
        this.userId= userID;
        this.taskQuestionIds = [];
        this.responseTexts= [];
    }

    addToResponseTexts(userResponse){
        this.responseTexts.push(userResponse);
    }

    addToQuestionIds(questionId){
        this.taskQuestionIds.push(questionId);
    }
}