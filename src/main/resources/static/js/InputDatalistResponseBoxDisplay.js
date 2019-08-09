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
        console.log("the answer is: " + this.correctResponse);
        //don't currently need a pointer to this datalist
        let possibleResponsesDatalist = buildDatalistElement(id, defaultResponses);
        //need a pointer to this textbox to check answers
        let inputBoxSize = inputBoxAutoSize(defaultResponses);
        this.inputTextbox = buildInputTextbox(id, possibleResponsesDatalist.getAttribute("id"), inputBoxSize);
        this.element = buildElement(id, possibleResponsesDatalist, this.inputTextbox);

        let feedbackTextArea = document.createElement("div");
        feedbackTextArea.id = "feedbackTextArea";
        this.textArea = feedbackTextArea;
        this.element.appendChild(feedbackTextArea);
    }

    checkCurrentResponse(response, unsureShowsCorrect, questionId) {
        if (this.inputTextbox.value !== ResponseResult.blank) {
            response.addToResponseTexts(this.inputTextbox.value);
            addToResponseIds(response, questionId);
        }
        let returnResponse = checkAnyResponse(this.correctResponse, this.inputTextbox.value);
        addToTypesIncorrect(returnResponse, this.type, response.typesIncorrect);

        this.textArea.innerHTML = displayCheckedResponse(returnResponse, this.correctResponse, unsureShowsCorrect);

        if (returnResponse === ResponseResult.correct) {
            disableElement(this.inputTextbox);
        } else if (returnResponse === ResponseResult.unsure) {
            disableElement(this.inputTextbox);
        }
        return returnResponse;
    }

    recordCurrentResponse(response) {
        if (this.inputTextbox.value !== ResponseResult.blank) {
            response.addToResponseTexts(this.inputTextbox.value);
        }
        return this.inputTextbox.value;
    }
}

function buildElement(id, possibleResponseDatalist, inputTextbox) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(possibleResponseDatalist);
    element.appendChild(inputTextbox);

    return element;
}

function addToTypesIncorrect(correctness, type, typesIncorrect) {
    if (correctness === ResponseResult.correct || correctness === ResponseResult.blank) {

    } else {
        if (!typesIncorrect.includes(type)) {
            typesIncorrect.push(type);
        }
    }

}

function displayCheckedResponse(correctness, correctResponse, unsureShowsCorrect) {
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
    inputTextbox.setAttribute("id", id);
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

function inputBoxAutoSize(listOfStrings) {
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
