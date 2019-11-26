class QuestionController{

    constructor(questionText=null){
        if (questionText === null){
            this.questionIsInput = true;
            this.inputElement = createInputQuestionElement();
            this.element = createLabeledElement(this.inputElement);
        }
        else {
            this.questionIsInput = false;
            this.element = createFixedQuestionElement(questionText);
        }
    }

    getResponse(){
        if (this.questionIsInput){
            return this.inputElement.value;
        }
        else {
            return null;
        }
    }
}

function createInputQuestionElement(){
    let input = document.createElement("input");
    input.type = "text";
    input.value= "";
    input.size= 50;
    input.id= "inputQuestionArea";
    return input;
}

function createLabeledElement(inputElementToLabel){
    let inputElement = document.createElement("div");

    //label to note it is a question is expected (since you can also type answers)
    let inputTextboxLabel = document.createElement("label");
    inputTextboxLabel.setAttribute("for", inputElementToLabel.id);
    inputTextboxLabel.textContent = "Q:";
    inputElement.appendChild(inputTextboxLabel);
    inputElement.appendChild(inputElementToLabel);
    return inputElement;
}

function createFixedQuestionElement(questionText){
    let questionTextArea = document.createElement("text");
    questionTextArea.textContent = questionText;
    questionTextArea.id= "fixedQuestionArea";
    return questionTextArea;
}