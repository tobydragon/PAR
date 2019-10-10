class QuestionAreaDisplay {
    constructor(questionObject, response, responseList) {
        this.response= response;
        this.answerBox = new InputDatalistResponseBoxDisplay(questionObject.id + "ResponseBox", questionObject.possibleAnswers, questionObject.correctAnswer, questionObject.type);
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
        if(this.questionTextArea.id==="questionTextAreaInput"){
            this.response.setQuestionText(this.questionTextArea.value);
        }
        return this.answerBox.recordCurrentResponse(this.response);
    }

    checkCurrentResponse(unsureShowsCorrectAnswer, responseList){
        return this.answerBox.checkCurrentResponse(this.response, unsureShowsCorrectAnswer, responseList);
    }
}

function buildQuestionAreaElement(id, questionTextArea, answerBoxElement) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(questionTextArea);
    element.appendChild(answerBoxElement);
    element.classList.add("pad5");


    return element;
}

function buildQuestionAreaElementUnchangable(questionText){
    let questionTextAreaUnchangable = document.createElement("text");
    questionTextAreaUnchangable.textContent = questionText;
    questionTextAreaUnchangable.id= "questionTextAreaFixed";
    return questionTextAreaUnchangable;
}

function createQuestionTextInputElement(){
    let input = document.createElement("input");
    input.type = "text";
    input.value= "What structure is circled?";
    input.size= 50;
    input.id= "questionTextAreaInput";
    return input;
}

function buildQuestionAreas(questionObjectList, responseList) {
    let questionAreaList = [];
    for (let questionObject of questionObjectList) {
        let newResponse= responseList.addToQuestionResponses(questionObject.id);
        questionAreaList.push(new QuestionAreaDisplay(questionObject, newResponse, responseList));
    }
    return questionAreaList;
}
