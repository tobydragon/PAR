class QuestionArea {
    constructor(questionObject){
        this.answerBox = new TextEntryResponseBox(questionObject.id+"ResponseBox", questionObject.possibleAnswers, questionObject.correctAnswer);
        this.element = buildQuestionAreaElement(questionObject.id, questionObject.questionText, this.answerBox);
    }
}

function buildQuestionAreaElement(id, questionText, answerBox){
    let element = document.createElement("div");
    element.setAttribute("id", id);

    let questionTextArea = document.createElement("text");
    questionTextArea.textContent = questionText;

    element.appendChild(questionTextArea);
    element.appendChild(answerBox.element);
    return element;
}