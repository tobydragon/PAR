class TestServerComm{

    constructor(imageTaskFilename){
        this.listOfImageTasks = readJson(imageTaskFilename);
        this.currentIndex = 0;
    }

    nextStudentImageTask(studId, responseHandler){
        let imageTaskModel = this.listOfImageTasks[this.currentIndex];
        if (this.currentIndex < this.listOfImageTasks.length){
            this.currentIndex++;
        }
        else {
            this.currentIndex=0;
        }
        responseHandler.replaceImageTaskModel(imageTaskModel);
    }

    submitImageTaskResponse(imageTaskToSubmit){
        console.log("Object to send:\n" + imageTaskToSubmit);
    }
}