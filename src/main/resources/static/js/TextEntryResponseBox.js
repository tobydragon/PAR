'use strict';

const ResponseResult = {
    correct :"correct",
    incorrect :"incorrect",
    unsure :"unsure",
    blank:""
};

class TextEntryResponseBox {

    constructor (id, defaultResponses, correctResponse){
        this.id = id;
        this.correctResponse = correctResponse;
        //don't currently need a pointer to this datalist
        let possibleResponsesDatalist = buildDatalistElement(id, defaultResponses);
        //need a pointer to this textbox to check answers
        this.inputTextbox = buildInputTextbox(id, possibleResponsesDatalist.getAttribute("id"));
        this.element = buildElement(id, possibleResponsesDatalist, this.inputTextbox);
    }

    checkCurrentResponse(){
        return checkAnyResponse(this.correctResponse, this.inputTextbox.value);
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
        return ResponseResult.correct;
    }
    else if (ResponseResult.unsure.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        return ResponseResult.unsure;
    }
    else if (ResponseResult.blank.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        return ResponseResult.blank;
    }
    else {
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