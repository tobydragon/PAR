'use strict';

const ResponseResult = {
    correct :"correct",
    incorrect :"incorrect",
    unsure :"unsure",
    blank:""
};

class InputDatalistResponseBoxDisplay {

    constructor (id, defaultResponses, correctResponse){
        this.id = id;
        this.correctResponse = correctResponse;
        //don't currently need a pointer to this datalist
        let possibleResponsesDatalist = buildDatalistElement(id, defaultResponses);
        //need a pointer to this textbox to check answers
        this.inputTextbox = buildInputTextbox(id, possibleResponsesDatalist.getAttribute("id"));
        this.element = buildElement(id, possibleResponsesDatalist, this.inputTextbox);

        let feedbackTextArea = document.createElement("div");
        feedbackTextArea.id= "feedbackTextArea";
        this.element.appendChild(feedbackTextArea);
    }

    checkCurrentResponse(response){
        response.addToResponseTexts(this.inputTextbox.value);
        return checkAnyResponse(this.correctResponse, this.inputTextbox.value, this.element);
    }
}

function buildElement(id, possibleResponseDatalist, inputTextbox){
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(possibleResponseDatalist);
    element.appendChild(inputTextbox);

    return element;
}

function checkAnyResponse(correctResponse, actualResponse){
    if (correctResponse.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        document.getElementById("feedbackTextArea").innerHTML = '<font color=\"green\">Your answer is: Correct</font>';
        return ResponseResult.correct;
    }
    else if (ResponseResult.unsure.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        document.getElementById("feedbackTextArea").innerHTML = "<font color=\"#663399\">The correct answer is " + correctResponse+'</font>';
        return ResponseResult.unsure;
    }
    else if (ResponseResult.blank.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        return ResponseResult.blank;
    }
    else {
        document.getElementById("feedbackTextArea").innerHTML = '<font color=\"red\">Your answer is: Incorrect</font>';
        return ResponseResult.incorrect;
    }

}

function buildDatalistElement(questionId, possibleResponses){
    let datalist = document.createElement("datalist");
    datalist.setAttribute("id", questionId + "Datalist");
    for (let optionText of possibleResponses){
        datalist.appendChild(buildOptionElement(optionText));
    }
    datalist.appendChild(buildOptionElement("unsure"));
    return datalist;
}

function buildOptionElement(optionText){
    let optionElement = document.createElement("option");
    optionElement.setAttribute("value", optionText);
    return optionElement;
}

/**
 * @param datalistId an id of a datalist tagged id that already exists in the document
 * @pre need to call buildDatalistElement before building this and use the id sent to buildDatalistElement
 */
function buildInputTextbox(id, datalistId){
    let inputTextbox = document.createElement("input");
    inputTextbox.type = "text";
    inputTextbox.setAttribute("id", id);
    inputTextbox.setAttribute("list", datalistId);
    return inputTextbox;
}