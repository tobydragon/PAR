class QuestionAndAnswerView {
    constructor(qaModel) {
        this.questionView = new QuestionView(qaModel.questionText);
        this.answerView = new AnswerView(qaModel);
        this.element = buildQuestionAreaElement(qaModel.id + "QuestionAndAnswerView", this.questionView.element, this.answerView.element);
    }
}

function buildQuestionAreaElement(id, questionView, answerView) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(questionView);
    element.appendChild(answerView);
    element.classList.add("pad5");
    return element;
}

