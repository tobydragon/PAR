class Response{

    constructor(userID){
        this.userId= userID;
        this.taskQuestionIds = [];
        this.responseTexts=[];
        this.typesIncorrect= [];
        this.questionTexts=[]
    }

    addToResponseTexts(userResponse){
        this.responseTexts.push(userResponse);
    }

    addToQuestionIds(questionId){
        this.taskQuestionIds.push(questionId);
    }

    addToQuestionTexts(questionText){
        this.questionTexts.push(questionText);
    }
}