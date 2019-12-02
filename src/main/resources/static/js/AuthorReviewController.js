class AuthorReviewController {

    constructor(userId, serverComm) {
        this.serverComm = serverComm;
        this.userId = userId;
        //the elements built in this must persist and be mutated, because it is referenced from the html
        this.buildElements(userId);
        this.loadAllImageTasks();
    }

    //side effects through callback loadAllImageTasksFromModel
    loadAllImageTasks() {
        this.serverComm.loadAllImageTasksFromModel(this.userId, this);
    }

    //callback upon server response to loadAllImageTasks
    loadAllImageTasksFromModel(newImageTaskListModel) {
        this.imageTaskControllerList = [];
        for (let imageTaskModel of newImageTaskListModel) {
            let imageTaskController = new ImageTaskController(imageTaskModel, this.userId);
            imageTaskController.showAllLevelFollowupQuestions();
            this.imageTaskElement.appendChild(imageTaskController.element);
            this.imageTaskControllerList.push(imageTaskController)
        }
    }

    buildElements(authorId) {
        this.imageTaskElement = document.createElement("div");
        this.imageTaskElement.setAttribute("imageTaskView", "id");

        this.element = document.createElement("div");
        this.element.setAttribute(authorId + "View", "id");
        this.element.appendChild(this.imageTaskElement);
    }

    transferAuthoredQuestionsToStudents() {
        this.serverComm.transferAuthoredQuestionsToStudents();
        this.clearAllImageTasks();
    }

    clearAllImageTasks() {
        while (this.imageTaskElement.firstChild) {
            this.imageTaskElement.removeChild(this.imageTaskElement.firstChild);
        }
        this.imageTaskControllerList = [];
    }
}