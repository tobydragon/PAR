class Response{
    #userId;
    #taskQuestionIds;
    #responseTexts;

    constructor(userID, questionIDs){
        this.userId= userID;
    }

    addToResponseTexts(userResponsesList){
        this.#responseTexts=userResponsesList;
    }

    setQuestionIds(listOfQuestionIds){
        this.taskQuestionIds= listOfQuestionIds;
    }
}