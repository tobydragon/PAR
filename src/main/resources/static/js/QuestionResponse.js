class QuestionResponse{
    constructor(id){
        this.id= id;
        this.responseText= null;
    }

    setResponseText(response){
        this.responseText= response;
    }

    setQuestionText(question){
        this.questionText= question;
    }
}