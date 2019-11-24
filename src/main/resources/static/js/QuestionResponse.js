class QuestionResponse{
    constructor(questionId,answerText,questionText=null){
        this.questionId=questionId;
        this.responseText=answerText;
        if(questionText!=null){
            this.questionText=questionText;
        }
    }

}