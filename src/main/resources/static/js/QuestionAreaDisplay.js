class QuestionAreaDisplay {
    constructor(questionObject, response) {
        this.answerBox = new InputDatalistResponseBoxDisplay(questionObject.id + "ResponseBox", questionObject.possibleAnswers, questionObject.correctAnswer, questionObject.type);
        if(questionObject.questionText!=null){
            this.element=buildCustomQuestionAreaElement(questionObject.questionText)
        }else{
            this.element = buildQuestionAreaElement(questionObject.id, questionObject.questionText, this.answerBox.element);
        }

        if (questionObject.hasOwnProperty("followupQuestions")) {
            this.followUpAreas = buildQuestionAreas(questionObject.followupQuestions, response);
        } else {
            this.followUpAreas = [];
        }
    }

    addFollowupQuestions() {
        let followupElement = document.createElement("div");
        for (let area of this.followUpAreas) {
            followupElement.appendChild(area.element);
        }
        if (followupElement.childNodes.length > 0) {
            this.element.appendChild(followupElement);
        }
        followupElement.classList.add("tab");
        this.followup = followupElement;
    }
}
function buildCustomQuestionAreaElement(questionText){
    let customQuestionTextElement = document.createElement("text");
    customQuestionTextElement.textContent = questionText;
    customQuestionTextElement.id= "questionTextAreaFixed";
    return customQuestionTextElement;
}
function buildQuestionAreaElement(id, questionText, answerBoxElement) {
    let element = document.createElement("div");
    element.setAttribute("id", id);

    let questionTextArea = document.createElement("text");
    questionTextArea.textContent = questionText;

    element.appendChild(questionTextArea);
    element.appendChild(answerBoxElement);
    element.classList.add("pad5");


    return element;
}

function buildQuestionAreas(questionObjectList, response) {
    let questionAreaList = [];
    for (let questionObject of questionObjectList) {
        questionAreaList.push(new QuestionAreaDisplay(questionObject, response));
    }
    return questionAreaList;
}
