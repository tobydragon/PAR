class QuestionAndAnswerController {
    constructor(qaModel) {
        this.qaModel = qaModel;
        this.questionView = new QuestionController(qaModel.questionText);
        this.answerView = new AnswerController(qaModel);
        this.element = buildQuestionAndAnswerViewElement(qaModel.id + "QuestionAndAnswerController", this.questionView.element, this.answerView.element);
    }

    getResponse(){
        if (this.answerView.getCurrentAnswer() === ResponseResult.blank || this.questionView.getResponse() === ResponseResult.blank){
            return null;
        }
        else {
            return new QuestionResponse(this.qaModel.id, this.answerView.getCurrentAnswer(), this.questionView.getResponse());
        }
    }

    checkAnswerAndUpdateView(){
        return this.answerView.checkAnswerAndUpdateView();
    }

    showAnswer(){
        this.answerView.showCorrectAnswer();
    }
}

function buildQuestionAndAnswerViewElement(id, questionView, answerView) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(questionView);
    element.appendChild(answerView);
    element.classList.add("pad5");
    return element;
}

