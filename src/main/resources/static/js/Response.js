class Response{

    constructor(userID){
        this.userId= userID;
        this.taskQuestionIds = [];
        this.responseTexts= [];
        this.typesIncorrect= new Set();
    }

    addToResponseTexts(userResponse){
        this.responseTexts.push(userResponse);
    }

    addToQuestionIds(questionId){
        this.taskQuestionIds.push(questionId);
    }
}