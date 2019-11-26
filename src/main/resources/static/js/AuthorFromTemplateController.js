class AuthorFromTemplateController {

    constructor(userId, serverComm) {
        this.serverComm = serverComm;
        this.userId = userId;
        //the elements built in this must persist and be mutated, because it is referenced from the html
        this.buildElements(userId);
        this.loadNextImageTask();
    }

    //side effects through callback authorImageTaskSubmitted
    submitAndNext() {
        let imageTaskResponse = this.imageTaskView.getResponse();
        if (imageTaskResponse.questionResponses.length > 0) {
            this.serverComm.submitAuthorImageTaskResponse(imageTaskResponse, this);
        }
    }

    //callback that happens after a response from submission is received
    authorImageTaskSubmitted(){
        this.loadNextImageTask();
    }

    //side effects through callback replaceImageTaskModel
    loadNextImageTask() {
        this.serverComm.nextAuthorImageTask(this.userId, this);
    }

    //callback upon server response to loadNextImageTask
    replaceImageTaskModel(newImageTaskModel) {
        this.imageTaskView = new ImageTaskController(newImageTaskModel, this.userId);
        this.imageTaskView.showAllLevelFollowupQuestions();
        if (this.imageTaskElement.firstChild) {
            this.imageTaskElement.removeChild(this.imageTaskElement.firstChild);
        }
        this.imageTaskElement.appendChild(this.imageTaskView.element);
    }

    buildElements(authorId) {
        this.imageTaskElement = document.createElement("div");
        this.imageTaskElement.setAttribute("imageTaskView", "id");

        this.element = document.createElement("div");
        this.element.setAttribute(authorId + "View", "id");
        this.element.appendChild(this.imageTaskElement);
    }
}