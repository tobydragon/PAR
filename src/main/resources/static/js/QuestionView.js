class QuestionView{

    constructor(questionText=null){
        if (questionText === null){
            this.questionIsInput = true;
            this.element = createInputQuestionElement();
        }
        else {
            this.questionIsInput = false;
            this.element = createFixedQuestionElement(questionText);
        }
    }

    getResponse(){
        if (this.questionIsInput){
            return this.element.value;
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

function createFixedQuestionElement(questionText){
    let questionTextArea = document.createElement("text");
    questionTextArea.textContent = questionText;
    questionTextArea.id= "fixedQuestionArea";
    return questionTextArea;
}