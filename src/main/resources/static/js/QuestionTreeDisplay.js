class QuestionTreeDisplay {

    constructor(questionObject, response, responseList) {
        this.response= response;
        this.answerBox = new AnswerView(questionObject.id + "ResponseBox", questionObject.possibleAnswers, questionObject.correctAnswer, questionObject.type);
        if(questionObject.questionText!=null) {
            this.questionTextArea = buildQuestionAreaElementUnchangable(questionObject.questionText);
        } else {
            this.questionTextArea= createQuestionTextInputElement();
        }
        this.element = buildQuestionAreaElement(questionObject.id, this.questionTextArea, this.answerBox.element);
        if (questionObject.hasOwnProperty("followupQuestions")) {
            this.followUpAreas = buildQuestionAreas(questionObject.followupQuestions, responseList);
        } else {
            this.followUpAreas = [];
        }
    }
}