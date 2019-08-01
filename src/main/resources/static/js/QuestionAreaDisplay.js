class QuestionAreaDisplay {
    constructor(questionObject, response, responseList) {
        this.response= response;
        this.answerBox = new InputDatalistResponseBoxDisplay(questionObject.id + "ResponseBox", questionObject.possibleAnswers, questionObject.correctAnswer, questionObject.type);
        this.element = buildQuestionAreaElement(questionObject.id, questionObject.questionText, this.answerBox.element);
        if (questionObject.hasOwnProperty("followupQuestions")) {
            this.followUpAreas = buildQuestionAreas(questionObject.followupQuestions, responseList);
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

    recordCurrentResponse(){
        return this.answerBox.recordCurrentResponse(this.response);
    }

    checkCurrentResponse(unsureShowsCorrectAnswer, responseList){
        return this.answerBox.checkCurrentResponse(this.response, unsureShowsCorrectAnswer, responseList);
    }
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

function buildQuestionAreas(questionObjectList, responseList) {
    let questionAreaList = [];
    for (let questionObject of questionObjectList) {
        let newResponse= responseList.addToQuestionResponses(questionObject.id);
        questionAreaList.push(new QuestionAreaDisplay(questionObject, newResponse, responseList));
    }
    return questionAreaList;
}
