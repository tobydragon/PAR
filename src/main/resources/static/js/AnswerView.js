'use strict';

const ResponseResult = {
    correct: "correct",
    incorrect: "incorrect",
    unsure: "unsure",
    blank: ""
};

class AnswerView {

    constructor(qaModel, qaSettings=null) {
        this.qaModel = qaModel;
        this.qaSettings = qaSettings;
        this.id = qaModel.id + "AnswerView";
        //let because don't need a pointer to this currently...
        let possibleResponsesDatalist = buildDatalistElement(this.id, qaModel.defaultAnswers);
        let inputBoxSize = calcTextSizeFromPossStrings(qaModel.defaultAnswers);

        //need a pointer to this textbox to check answers
        this.inputTextbox = buildInputTextbox(this.id, possibleResponsesDatalist.getAttribute("id"), inputBoxSize);
        this.feedbackArea = buildFeedbackArea();
        this.element = buildElement(this.id, possibleResponsesDatalist, this.inputTextbox, this.feedbackArea);

    }

    getCurrentAnswer(){
        return this.inputTextbox.value;
    }

    checkAnswerAndUpdateView() {
        let returnResponse = checkAnyResponse(this.qaModel.correctResponse, this.inputTextbox.value);
        this.feedbackArea.innerHTML = makeFeedbackHtml(returnResponse, this.qaModel.correctResponse, this.qaSettings.unsureShowsCorrect);

        if (returnResponse === ResponseResult.correct) {
            disableElement(this.inputTextbox);
        } else if (returnResponse === ResponseResult.unsure) {
            disableElement(this.inputTextbox);
        }

        return returnResponse;
    }

}

function buildElement(id, possibleResponseDatalist, inputTextbox, feedbackArea) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(possibleResponseDatalist);
    element.appendChild(inputTextbox);
    element.appendChild(feedbackArea);
    return element;
}

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