class QATreeController {

    constructor(qaModel) {
        this.qaController = new QuestionAndAnswerController(qaModel);
        if (qaModel.hasOwnProperty("followupQuestions")) {
            this.followupQATreeControllers = buildQATreeControllerList(qaModel.followupQuestions);
        } else {
            this.followupQATreeControllers = [];
        }
        this.element = buildQATreeViewElement(qaModel.id + "QATreeController", this.qaController.element);
    }

    showNextLevelFollowupQuestions() {
        this.element.appendChild(buildQATreeListElement(this.followupQATreeControllers));
    }

    showAllLevelFollowupQuestions(){
        showAllLevelFollowupQuestions(this);
    }

    showThisLevelAnswer(){
        this.qaController.showAnswer();
    }

    showAllAnswers(){
        showAllAnswers(this);
    }


    checkAnswersAndUpdateView(){
        let result = this.qaController.checkAnswerAndUpdateView();
        if (result === ResponseResult.correct){
            this.showNextLevelFollowupQuestions();
        }
        for (let followupView of this.followupQATreeControllers) {
            followupView.checkAnswersAndUpdateView();
        }
    }

    areAnswerBoxAndAllFollowupAnswerBoxesDisabled(){
        if (!this.qaController.isAnswerBoxDisabled()){
            return false;
        }
        else {
            for (let followupController of this.followupQATreeControllers) {
                if (!followupController.areAnswerBoxAndAllFollowupAnswerBoxesDisabled()){
                    return false;
                }
            }
            return true;
        }
    }

    getResponse(){
        let response = [];
        putNonBlankResponsesInFlatList(this, response);
        return response;
    }
}

function buildQATreeViewElement(id, qaViewElement) {
    let element = document.createElement("div");
    element.setAttribute("id", id);
    element.appendChild(qaViewElement);
    return element;
}

function buildQATreeControllerList(qaModelList) {
    let qaTreeControllerList = [];
    for (let qaModel of qaModelList) {
        qaTreeControllerList.push(new QATreeController(qaModel));
    }
    return qaTreeControllerList;
}

function getResponsesFromAllQATreesInFlatList(qaTreeViewList){
    let responses = [];
    for (let qaTreeView of qaTreeViewList){
        putNonBlankResponsesInFlatList(qaTreeView, responses);
    }
    return responses;
}

function buildQATreeListElement(qaTreeViewList){
    let listElement = document.createElement("div");
    for (let followupQATreeView of qaTreeViewList) {
        listElement.appendChild(followupQATreeView.element);
    }
    listElement.classList.add("tab");
    return listElement;
}

function putNonBlankResponsesInFlatList(qaTreeView, questionResponseList){
    let currentResponse = qaTreeView.qaController.getResponse();
    if (currentResponse != null) {
        questionResponseList.push(currentResponse);
        for (let followup of qaTreeView.followupQATreeControllers) {
            putNonBlankResponsesInFlatList(followup, questionResponseList);
        }
    }
}

function showAllLevelFollowupQuestions(qaTreeView){
    qaTreeView.showNextLevelFollowupQuestions();
    for (let followup of qaTreeView.followupQATreeControllers) {
        showAllLevelFollowupQuestions(followup);
    }
}

function showAllAnswers(qaTreeController){
    qaTreeController.showThisLevelAnswer();
    for (let followup of qaTreeController.followupQATreeControllers) {
        showAllAnswers(followup);
    }
}