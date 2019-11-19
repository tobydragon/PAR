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
        this.qaView.checkAnswerAndUpdateView();
        for (let followupView of this.followupQATreeViews) {
            followupView.checkAnswersAndUpdateView();
        }
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