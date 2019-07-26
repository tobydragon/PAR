class PageDisplay {

    constructor(pageSettings) {
        this.imageTaskDisplay = null;
        this.userId = null;
        //settings
        this.pageSettings= pageSettings;
        this.scoreType = pageSettings.scoreType;
        this.showScore = pageSettings.showScore;
        this.imageTaskSettings=null;
    }

    setIsAuthor() {
        this.isAuthor = setIsAuthor(this.userId);
        if(!this.isAuthor){
            document.getElementById("submitAuthorButton").classList.add("hide");
        }
        document.getElementById("authorReviewSubmitButton").classList.add("hide");
    }

    nextImageTask() {
        try {
            this.imageTaskSettings = readJson("api/getImageTaskSettings?userId=" + this.userId);
            if (this.isAuthor) {
                var imageTaskJSON = readJson("api/nextAuthorImageTask?userId=" + this.userId);
            } else {
                var imageTaskJSON = readJson("api/nextImageTask?userId=" + this.userId);
            }
            this.imageTaskDisplay = new ImageTaskDisplay(imageTaskJSON, this.userId, this.imageTaskSettings, this.isAuthor, "myCanvas", this.pageSettings);

        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }

    }

    logout() {
        this.userId = null;
        return location.replace('/login');
    }

    authorSubmitFinal(){
        let request = new XMLHttpRequest();
        request.open("GET", "/api/transferAuthoredQuestionsToStudents", false);
        request.send(null);
    }

    enterAuthorReview(){
        //TODO need a url in the rest controller that returns all the image tasks that the author wants to submit
        //let listOfImageTaska= readJson();
        enterAuthorReview(listOfImageTasks, this.userId, this.imageTaskSettings, this.isAuthor, this.pageSettings);
    }

}

function enterAuthorReview(listOfImageTasks, userId, imageTaskSettings, isAuthor, pageSettings){
    document.getElementById("authorReviewSubmitButton").classList.remove("hide");
    document.getElementById("submitButton").classList.add("hide");

    for(var i=0; i<listOfImageTasks.length; i++){
        let current=listOfImageTasks[i];
        let canvasName= "canvas"+i;
        let newImageTask= new ImageTaskDisplay(current, userId, imageTaskSettings, isAuthor, canvasName, pageSettings);
        newImageTask.lockInCorrectAnswers();
    }
}

function logout() {
    return location.replace('/login');
}

function setIsAuthor(userId) {
    if (userId === "author") {
        return true;
    } else {
        return false;
    }
}
