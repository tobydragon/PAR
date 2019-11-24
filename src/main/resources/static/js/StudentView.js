class StudentView{

    constructor(userId, serverComm){
        this.serverComm = serverComm;
        this.userId = userId;
        //the elements built in this must persist and be mutated, because it is referenced from the html
        this.buildElements(userId);
        this.loadNextImageTask();
    }

    submitCurrentResponse(){
        this.imageTaskView.checkAnswersAndUpdateView();
        let imageTaskResponse = this.imageTaskView.getResponse();
        if (imageTaskResponse.questionResponses.length > 0) {
            this.serverComm.submitImageTaskResponse(imageTaskResponse);
        }
    }

    loadNextImageTask() {
        this.serverComm.nextStudentImageTask(this.userId, this);
    }

    replaceImageTaskModel(newImageTaskModel){
        this.imageTaskView = new ImageTaskView(newImageTaskModel, this.userId);
        if (this.imageTaskElement.firstChild){
            this.imageTaskElement.removeChild(this.imageTaskElement.firstChild);
        }
        this.imageTaskElement.appendChild(this.imageTaskView.element);
    }

    buildElements(studId){
        this.imageTaskElement = document.createElement("div");
        this.imageTaskElement.setAttribute("imageTaskView", "id");

        this.element = document.createElement("div");
        this.element.setAttribute(studId+"View", "id");
        this.element.appendChild(this.imageTaskElement);
    }
}

