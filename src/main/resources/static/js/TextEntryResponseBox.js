class TextEntryResponseBox {

    constructor (id, possibleResponses, correctResponse){
        this.id = id;
        this.possibleResponses = possibleResponses;
        this.correctResponse = response;
        this.possibleResponsesDatalist = createDatalist(id, possibleResponses);
        this.inputTextbox = createInputTextbox(id, this.possibleResponsesDatalist.getAttribute("id"));
    }

    appendToElement(whereToAppendId){
        document.getElementById(whereToAppendId).appendChild(this.possibleResponsesDatalist);
        document.getElementById(whereToAppendId).appendChild(this.inputTextbox);
    }
}

function createDatalist(id, possibleResponses){
    let datalist = document.createElement("datalist");
    datalist.setAttribute("id", "responseDataList"+id);
    for (let optionText of possibleResponses){
        datalist.appendChild(createOptionElement(optionText));
    }
    datalist.appendChild(createOptionElement("unsure"));
    console.log ("Datalist created: "   + datalist.getElementsByClassName("option"));
    return datalist;
}

function createOptionElement(optionText){
    let optionElement = document.createElement("option");
    optionElement.setAttribute("value", optionText);
    console.log ("option created: "   + optionElement);
    return optionElement;
}

function createInputTextbox(id, datalistId){
    let inputTextbox = document.createElement("input");
    inputTextbox.type = "text";
    inputTextbox.setAttribute("id", id);
    inputTextbox.setAttribute("list", datalistId);
    return inputTextbox;
}