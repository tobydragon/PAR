class TestServerComm{

    constructor(imageTaskFilename, authorTaskFilename){
        this.studentImageTasks = readJson(imageTaskFilename);
        this.studIndex = 0;
        this.authorImageTasks = readJson(authorTaskFilename);
        this.authorIndex = 0;
    }

    nextStudentImageTask(studId, responseHandler){
        let imageTaskModel = this.studentImageTasks[this.studIndex];
        if (this.studIndex < this.studentImageTasks.length-1){
            this.studIndex++;
        }
        else {
            this.studIndex=0;
        }
        responseHandler.replaceImageTaskModel(imageTaskModel);
    }

    submitImageTaskResponse(imageTaskToSubmit){
        console.log("Object being sent:\n" + JSON.stringify(imageTaskToSubmit));
    }

    nextAuthorImageTask(studId, responseHandler){
        let imageTaskModel = this.authorImageTasks[this.authorIndex];
        if (this.authorIndex < this.authorImageTasks.length-1){
            this.authorIndex++;
        }
        else {
            this.authorIndex=0;
        }
        responseHandler.replaceImageTaskModel(imageTaskModel);
    }

    submitAuthorImageTaskResponse(imageTaskToSubmit, responseHandler){
        console.log("Object to send:\n" + JSON.stringify(imageTaskToSubmit));
        responseHandler.authorImageTaskSubmitted();
    }

    loadAllImageTasksFromModel(studId, responseHandler){
        responseHandler.loadAllImageTasksFromModel(this.authorImageTasks);
    }

    transferAuthoredQuestionsToStudents(){
        console.log("transfer triggered\n");
    }

    updateScore(studId, responseHandler){
        let model={
            plane:'__XO',
            struct:'X__O'
        };
        responseHandler.updateScoreWithModel(model);
    }
}