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
        document.getElementById("canvasArea").innerText = "";
        try {
            let request = new XMLHttpRequest();
            request.open("GET", "/api/transferAuthoredQuestionsToStudents", false);
            request.send(null);

            document.getElementById("errorFeedback").innerHTML = "<font color=\"#663399\"> Questions moved to student's pool</font>";
            document.getElementById("questionSet").innerText = "";
        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }
    }

    enterAuthorReview(){
        document.getElementById("questionSet").innerHTML = " ";
        document.getElementById("canvasArea").innerText = "";
        let listOfImageTasks= readJson("/api/authoredQuestions");
        enterAuthorReview(listOfImageTasks, this.userId, this.imageTaskSettings, this.isAuthor, this.pageSettings);
    }

}

function enterAuthorReview(listOfImageTasks, userId, imageTaskSettings, isAuthor, pageSettings){
    document.getElementById("authorReviewSubmitButton").classList.remove("hide");
    document.getElementById("submitButton").classList.add("hide");

    for(var i=0; i<listOfImageTasks.length; i++){
        let current=listOfImageTasks[i];
        let canvasName= "canvas"+i;
        if(i>0) {
            formatAuthorReviewQuestions(i);
        }
        let newImageTask= new ImageTaskDisplay(current, userId, imageTaskSettings, isAuthor, canvasName, pageSettings);
        newImageTask.lockInCorrectAnswers();
    }
}

function formatAuthorReviewQuestions(number){
    let space = document.createElement("br");
    let space2 = document.createElement("br");
    let space3 = document.createElement("br");
    let element = document.createElement("div");
    let header = document.createElement("h2");
    header.textContent = "Question Set " + (number + 1);
    element.appendChild(header);
    document.getElementById("questionSet").appendChild(space);
    document.getElementById("questionSet").appendChild(space2);
    document.getElementById("questionSet").appendChild(space3);
    document.getElementById("questionSet").appendChild(element);
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
