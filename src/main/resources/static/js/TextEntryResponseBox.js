class TextEntryResponseBox {

    constructor (id, possibleResponses, correctResponse){
        this.id = id;
        this.possibleResponses = possibleResponses;
        this.correctResponse = response;
        this.possibleResponsesDatalist = buildDatalist(id, possibleResponses);
        this.inputTextbox = buildInputTextbox(id, this.possibleResponsesDatalist.getAttribute("id"));
    }

    appendToElement(whereToAppendId){
        document.getElementById(whereToAppendId).appendChild(this.possibleResponsesDatalist);
        document.getElementById(whereToAppendId).appendChild(this.inputTextbox);
    }
}

function buildDatalist(id, possibleResponses){
    let datalist = document.createElement("datalist");
    datalist.setAttribute("id", "responseDataList"+id);
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
 */
function buildInputTextbox(id, datalistId){
    let inputTextbox = document.createElement("input");
    inputTextbox.type = "text";
    inputTextbox.setAttribute("id", id);
    inputTextbox.setAttribute("list", datalistId);
    return inputTextbox;
}