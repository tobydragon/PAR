'use strict';

const ResponseResult = {
    correct: "correct",
    incorrect: "incorrect",
    unsure: "unsure",
    blank: ""
};

const defaultQaSettings = {
    unsureShowsCorrect: true
};

class AnswerController {

    constructor(qaModel, qaSettings=defaultQaSettings) {
        this.qaModel = qaModel;
        this.qaSettings = qaSettings;
        this.id = qaModel.id + "AnswerController";
        //let because don't need a pointer to this currently...
        let possibleResponsesDatalist = buildDatalistElement(this.id, qaModel.possibleAnswers);
        let inputBoxSize = calcTextSizeFromPossStrings(qaModel.possibleAnswers);

        //need a pointer to this textbox to check answers
        this.inputTextbox = buildInputTextbox(this.id, possibleResponsesDatalist.getAttribute("id"), inputBoxSize);
        this.feedbackArea = buildFeedbackArea();
        this.element = buildElement(this.id, possibleResponsesDatalist, this.inputTextbox, this.feedbackArea);

    }

    getCurrentAnswer(){
        return this.inputTextbox.value;
    }

    //todo for refactor, get rid of makeFeedbackHTML changing innerHTML. func should be be appended to feedbackArea
    checkAnswerAndUpdateView() {
        let returnResponse = checkAnyResponse(this.qaModel.correctAnswer, this.inputTextbox.value);
        this.feedbackArea.innerHTML = makeFeedbackHtml(returnResponse, this.qaModel.correctAnswer, this.qaSettings.unsureShowsCorrect);

        if (returnResponse === ResponseResult.correct) {
            disableElement(this.inputTextbox);
        } else if (returnResponse === ResponseResult.unsure) {
            disableElement(this.inputTextbox);
        }
        return returnResponse;
    }

    showCorrectAnswer(){
        this.inputTextbox.value = this.qaModel.correctAnswer;
        disableElement(this.inputTextbox);
    }
}

function buildElement(id, possibleResponseDatalist, inputTextbox, feedbackArea) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(possibleResponseDatalist);

    //label to note it is an answer expected (since you can type questions in author mode)
    // let inputTextboxLabel = document.createElement("label");
    // inputTextboxLabel.setAttribute("for", inputTextbox.id);
    // inputTextboxLabel.textContent = "A:";
    // element.appendChild(inputTextboxLabel);

    element.appendChild(inputTextbox);
    element.appendChild(feedbackArea);
    return element;
}

//todo ideally makeFeedbackHTML should be returning an element rather than an HTML string
function makeFeedbackHtml(correctness, correctResponse, unsureShowsCorrect) {
    if (correctness === ResponseResult.correct) {
        return '<font color=\"green\">Your answer is: Correct</font>';
    } else if (correctness === ResponseResult.unsure) {
        if (unsureShowsCorrect) {
            return "<font color=\"#663399\">The correct answer is " + correctResponse + '</font>';
        } else {
            return "";
        }
    } else if (correctness === ResponseResult.blank) {
        return "";
    } else {
        return '<font color=\"red\">Your answer is: Incorrect</font>';
    }
}

function checkAnyResponse(correctResponse, actualResponse) {
    if (correctResponse.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        return ResponseResult.correct;
    } else if (ResponseResult.unsure.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        return ResponseResult.unsure;
    } else if (ResponseResult.blank.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        return ResponseResult.blank;
    } else {
        return ResponseResult.incorrect;
    }
}

function buildDatalistElement(questionId, possibleResponses) {
    let datalist = document.createElement("datalist");
    datalist.setAttribute("id", questionId + "Datalist");
    for (let optionText of possibleResponses) {
        datalist.appendChild(buildOptionElement(optionText));
    }
    return datalist;
}

function buildOptionElement(optionText) {
    let optionElement = document.createElement("option");
    optionElement.setAttribute("value", optionText);
    return optionElement;
}

/**
 * @param datalistId an id of a datalist tagged id that already exists in the document
 * @pre need to call buildDatalistElement before building this and use the id sent to buildDatalistElement
 */
function buildInputTextbox(id, datalistId, size) {

    let inputTextbox = document.createElement("input");
    inputTextbox.type = "text";
    inputTextbox.setAttribute("id", id+"inputBox");
    inputTextbox.setAttribute("name", id+"inputBox");
    inputTextbox.setAttribute("list", datalistId);
    inputTextbox.setAttribute("size", size);
    inputTextbox.classList.add("line-input-box");

    return inputTextbox;
}

function disableElement(elementToDisable) {
    return elementToDisable.disabled = true;
}

function enableElement(elementToDisable) {
    return elementToDisable.disabled = false;
}

function calcTextSizeFromPossStrings(listOfStrings) {
    let highestCharCount = 0;
    for (let aString of listOfStrings) {
        if (aString.length > highestCharCount) {
            highestCharCount = aString.length;
        }
    }
    //Margin size correcting
    let diff = highestCharCount * 0.16;
    highestCharCount = highestCharCount - diff;
    highestCharCount = Math.ceil(highestCharCount);
    if (highestCharCount <= 20) {
        return 20;
    }
    return highestCharCount;
}

function buildFeedbackArea(){
    let feedbackTextArea = document.createElement("div");
    feedbackTextArea.id = "feedbackTextArea";
    return feedbackTextArea;
}