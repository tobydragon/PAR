class NewResponse{
    constructor(userID){
        this.userId= userID;
        this.questionResponses= [];
        this.typesIncorrect= [];
    }

    addToQuestionResponses(questionId){
        let newQuestionResponse= new QuestionResponse(questionId);
        this.questionResponses.push(newQuestionResponse);
        return newQuestionResponse;
    }
}