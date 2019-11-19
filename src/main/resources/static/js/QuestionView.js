class QuestionView{

    constructor(questionText=null){
        if (questionText === null){
            this.questionIsInput = true;
            this.element = createQuestionTextInputElement();
        }
        else {
            this.questionIsInput = false;
            this.element = buildQuestionAreaElementUnchangable(questionText);
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

function createQuestionTextInputElement(){
    let input = document.createElement("input");
    input.type = "text";
    input.value= "";
    input.size= 50;
    input.id= "questionTextAreaInput";
    return input;
}

function buildQuestionAreaElementUnchangable(questionText){
    let questionTextArea = document.createElement("text");
    questionTextArea.textContent = questionText;
    questionTextArea.id= "questionTextAreaFixed";
    return questionTextArea;
}