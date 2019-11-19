class QATreeView {

    constructor(qaModel) {
        this.qaView = new QuestionAndAnswerView(qaModel);
        if (qaModel.hasOwnProperty("followupQuestions")) {
            this.followupQATreeViews = buildQATreeViewList(qaModel.followupQuestions);
        } else {
            this.followupQATreeViews = [];
        }
        this.element = buildQATreeViewElement(qaModel.id + "QATreeView", this.qaView.element);
    }

    showFollowupQuestions() {
        this.element.appendChild(buildQATreeListElement(this.followupQATreeViews));
    }

    checkAnswersAndUpdateView(){
        let result = this.qaView.checkAnswerAndUpdateView();
        if (result === ResponseResult.correct){
            this.showFollowupQuestions();
        }
        for (let followupView of this.followupQATreeViews) {
            followupView.checkAnswersAndUpdateView();
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

function buildQATreeViewList(qaModelList) {
    let qaTreeViewList = [];
    for (let qaModel of qaModelList) {
        qaTreeViewList.push(new QATreeView(qaModel));
    }
    return qaTreeViewList;
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
    let currentResponse = qaTreeView.qaView.getResponse();
    if (currentResponse != null) {
        questionResponseList.push(currentResponse);
        for (let followup of qaTreeView.followupQATreeViews) {
            putNonBlankResponsesInFlatList(followup, questionResponseList);
        }
    }
}