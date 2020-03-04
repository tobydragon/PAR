class QuestionAndAnswerController {
    constructor(qaModel) {
        this.qaModel = qaModel;
        this.questionController = new QuestionController(qaModel.questionText);
        this.answerController = new AnswerController(qaModel);
        this.element = buildQuestionAndAnswerViewElement(qaModel.id + "QuestionAndAnswerController", this.questionController.element, this.answerController.element);
    }

    getResponse(){
        if (this.answerController.getCurrentAnswer() === ResponseResult.blank || this.questionController.getResponse() === ResponseResult.blank){
            return null;
        }
        else {
            return new QuestionResponse(this.qaModel.id, this.answerController.getCurrentAnswer(), this.questionController.getResponse());
        }
    }

    checkAnswerAndUpdateView(){
        return this.answerController.checkAnswerAndUpdateView();
    }

    isAnswerBoxDisabled(){
        return this.answerController.isAnswerBoxDisabled();
    }

    showAnswer(){
        this.answerController.showCorrectAnswer();
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

