class QuestionView{

    constructor(questionText=null){
        if (questionText === null){
            this.element = createQuestionTextInputElement();
        }
        else {
            this.element = buildQuestionAreaElementUnchangable();
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