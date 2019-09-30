class QuestionResponse{
    constructor(questionId){
        this.questionId= questionId;
        this.responseText= null;
    }

    setResponseText(response){
        this.responseText= response;
    }

    setQuestionText(question){
        this.questionText= question;
    }
}