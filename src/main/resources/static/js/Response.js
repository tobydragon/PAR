class Response{
    #userId;
    #taskQuestionIds = [];
    #responseTexts= [];

    constructor(userID, questionIDs){
        this.userId= userID;
    }

    addToResponseTexts(userResponse){
        this.#responseTexts.push(userResponse);
    }

    addToQuestionIds(questionId){
        this.#taskQuestionIds.push(questionId);
    }
}