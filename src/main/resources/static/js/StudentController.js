class StudentController{

    constructor(userId, serverComm){
        this.serverComm = serverComm;
        this.userId = userId;
        //the elements built in this must persist and be mutated, because it is referenced from the html
        this.buildElements(userId);
        this.loadNextImageTask();
        this.updateScore();
    }

    //has side effect by calling submitImageTaskConfirmation upon response
    submitCurrentResponse(){
        this.imageTaskView.checkAnswersAndUpdateView();
        let imageTaskResponse = this.imageTaskView.getResponse();
        if (imageTaskResponse.questionResponses.length > 0) {
            this.serverComm.submitImageTaskResponse(imageTaskResponse, this);
        }
    }

    //triggered by server response to loadNextImageTask
    submitImageTaskConfirmation(){
        this.updateScore();
    }

    //has side effect by calling replaceImageTaskModel upon response
    loadNextImageTask() {
        this.serverComm.nextStudentImageTask(this.userId, this);
    }

    //triggered by server response to loadNextImageTask
    replaceImageTaskModel(newImageTaskModel){
        this.imageTaskView = new ImageTaskController(newImageTaskModel, this.userId);
        if (this.imageTaskElement.firstChild){
            this.imageTaskElement.removeChild(this.imageTaskElement.firstChild);
        }
        this.imageTaskElement.appendChild(this.imageTaskView.element);
    }

    //has side effect by calling updateScoreWithModel upon response
    updateScore() {
        this.serverComm.updateScore(this.userId, this);
    }

    //triggered by server response to updateScoreWithModel
    updateScoreWithModel(newScoreModel){
        if (this.scoreElement.firstChild){
            this.scoreElement.removeChild(this.scoreElement.firstChild);
        }
        this.scoreElement.appendChild(buildScoreElement(newScoreModel));
    }

    buildElements(studId){
        //this goes in the toolbar, doesn't need to be put in the element div.
        this.scoreElement = document.createElement("div");
        this.scoreElement.setAttribute("scoreView", "id");

        this.imageTaskElement = document.createElement("div");
        this.imageTaskElement.setAttribute("imageTaskView", "id");

        this.element = document.createElement("div");
        this.element.setAttribute(studId+"View", "id");
        this.element.appendChild(this.imageTaskElement);
    }

    logout(){
        location.replace('/login');
    }
}

