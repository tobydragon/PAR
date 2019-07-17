'use strict';

const ResponseResult = {
    correct: "correct",
    incorrect: "incorrect",
    unsure: "unsure",
    blank: ""
};

class InputDatalistResponseBoxDisplay {

    constructor(id, defaultResponses, correctResponse, type) {
        this.type = type;
        this.id = id;
        this.correctResponse = correctResponse;
        //don't currently need a pointer to this datalist
        let possibleResponsesDatalist = buildDatalistElement(id, defaultResponses);
        //need a pointer to this textbox to check answers
        this.inputTextbox = buildInputTextbox(id, possibleResponsesDatalist.getAttribute("id"));
        this.element = buildElement(id, possibleResponsesDatalist, this.inputTextbox);

        let feedbackTextArea = document.createElement("div");
        feedbackTextArea.id = "feedbackTextArea";
        this.textArea = feedbackTextArea;
        this.element.appendChild(feedbackTextArea);
    }

    checkCurrentResponse(response, unsureShowsCorrect) {
        response.addToResponseTexts(this.inputTextbox.value);
        return checkAnyResponse(this.correctResponse, this.inputTextbox.value, this.type, response.typesIncorrect, this.textArea, unsureShowsCorrect);
    }
}

function buildElement(id, possibleResponseDatalist, inputTextbox) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(possibleResponseDatalist);
    element.appendChild(inputTextbox);

    return element;
}

function checkAnyResponse(correctResponse, actualResponse, type, typesIncorrect, textArea, unsureShowsCorrect) {
    if (correctResponse.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        textArea.innerHTML = '<font color=\"green\">Your answer is: Correct</font>';
        return ResponseResult.correct;
    } else if (ResponseResult.unsure.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        if (unsureShowsCorrect) {
            textArea.innerHTML = "<font color=\"#663399\">The correct answer is " + correctResponse + '</font>';
        }
        if (!typesIncorrect.includes(type)) {
            typesIncorrect.push(type);
        }
        return ResponseResult.unsure;
    } else if (ResponseResult.blank.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        if (!typesIncorrect.includes(type)) {
            typesIncorrect.push(type);
        }
        return ResponseResult.blank;
    } else {
        textArea.innerHTML = '<font color=\"red\">Your answer is: Incorrect</font>';
        if (!typesIncorrect.includes(type)) {
            typesIncorrect.push(type);
        }
        return ResponseResult.incorrect;
    }

}

function checkAnyResponseRewritten(correctResponse, actualResponse, element) {
    if (correctResponse.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        element.classList.add("correct");
        element.textContent = "Your answer is: Correct";
        disableElement(element);
        return ResponseResult.correct;
    } else if (ResponseResult.unsure.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        element.classList.add("unsure");
        element.textContent = "The correct answer is " + correctResponse;
        disableElement(element);
        return ResponseResult.unsure;
    } else if (ResponseResult.blank.trim().toLowerCase() === actualResponse.trim().toLowerCase()) {
        return ResponseResult.blank;
    } else {
        element.classList.add("incorrect");
        element.textContent = "Your answer is: Incorrect";
        return ResponseResult.incorrect;
    }
}

function buildDatalistElement(questionId, possibleResponses) {
    let datalist = document.createElement("datalist");
    datalist.setAttribute("id", questionId + "Datalist");
    for (let optionText of possibleResponses) {
        datalist.appendChild(buildOptionElement(optionText));
    }
    datalist.appendChild(buildOptionElement("unsure"));
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
function buildInputTextbox(id, datalistId) {
    let inputTextbox = document.createElement("input");
    inputTextbox.type = "text";
    inputTextbox.setAttribute("id", id);
    inputTextbox.setAttribute("list", datalistId);
    return inputTextbox;
}

function disableElement(elementToDisable) {
    return elementToDisable.disabled = true;
}
