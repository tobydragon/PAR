class QATreeView {

    constructor() {
        if (questionObject.hasOwnProperty("followupQuestions")) {
            this.followUpAreas = buildQuestionAreas(questionObject.followupQuestions, responseList);
        } else {
            this.followUpAreas = [];
        }
    }

    addFollowupQuestions() {
        let followupElement = document.createElement("div");
        for (let area of this.followUpAreas) {
            followupElement.appendChild(area.element);
        }
        if (followupElement.childNodes.length > 0) {
            this.element.appendChild(followupElement);
        }
        followupElement.classList.add("tab");
        this.followup = followupElement;
    }
}