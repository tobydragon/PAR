class Response{

    constructor(userID, questionIDs){
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