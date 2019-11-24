class StudentView{

    constructor(userId){
        let listOfImageTasks = readJson("../../resources/author/SampleListOfImageTasks.json");
        let imageTaskModel = listOfImageTasks[0];

        this.userId = userId;
        //the elements built in this must persist and be mutated, because it is referenced from the html
        this.buildElements(userId);
        this.replaceImageTaskModel(imageTaskModel);
    }

    submitCurrentResponse(){
        this.imageTaskView.checkAnswersAndUpdateView();
        let imageTaskResponse = this.imageTaskView.getResponse();
        console.log(imageTaskResponse);

    }

    loadNextImageTask() {
        let listOfImageTasks = readJson("../../resources/author/SampleListOfImageTasks.json");
        let imageTaskModel = listOfImageTasks[1];
        this.replaceImageTaskModel(imageTaskModel);
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

