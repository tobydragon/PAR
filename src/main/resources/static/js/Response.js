class Response{

    constructor(userID, taskQuestionIds, responseTexts){
        this.userId= userID;
        this.taskQuestionIds = taskQuestionIds;
        this.responseTexts= responseTexts;
        this.typesIncorrect= [];
    }

    addToResponseTexts(userResponse){
        this.responseTexts.push(userResponse);
    }

    addToQuestionIds(questionId){
        this.taskQuestionIds.push(questionId);
    }
}