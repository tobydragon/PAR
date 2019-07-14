const ResponseResult = {
    correct :"correct",
    incorrect :"incorrect",
    unsure :"unsure"
}

class TextEntryResponseBox {

    constructor (id, defaultResponses, correctResponse){
        this.id = id;
        this.correctResponse = correctResponse;
        this.possibleResponsesDatalist = buildDatalistElement(id, defaultResponses);
        this.inputTextbox = buildInputTextbox(id, this.possibleResponsesDatalist.getAttribute("id"));
    }

    appendToElement(whereToAppendId){
        document.getElementById(whereToAppendId).appendChild(this.possibleResponsesDatalist);
        document.getElementById(whereToAppendId).appendChild(this.inputTextbox);
    }

    checkCurrentResponse(){
        return checkAnyResponse(this.correctResponse, this.inputTextbox.value);
    }
}

function checkAnyResponse(correctResponse, actualResponse){
    if (correctResponse.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        return ResponseResult.correct;
    }
    else if (ResponseResult.unsure.trim().toLowerCase() === actualResponse.trim().toLowerCase()){
        return ResponseResult.unsure;
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
    console.log ("Datalist created: "   + datalist.getElementsByClassName("option"));
    return datalist;
}

function buildOptionElement(optionText){
    let optionElement = document.createElement("option");
    optionElement.setAttribute("value", optionText);
    return optionElement;
}

/**
 * @param datalistId and id of a datalist tagged id that already exists in the document
 * @pre need to call buildDatalistElement before building this and use the id sent to buildDatalistElement
 */
function buildInputTextbox(id, datalistId){
    let inputTextbox = document.createElement("input");
    inputTextbox.type = "text";
    inputTextbox.setAttribute("id", id);
    inputTextbox.setAttribute("list", datalistId);
    return inputTextbox;
}