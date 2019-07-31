class PageDisplay {

    constructor(pageSettings) {
        this.imageTaskDisplay = null;
        this.userId = null;
        //settings
        this.pageSettings = pageSettings;
        this.scoreType = pageSettings.scoreType;
        this.showScore = pageSettings.showScore;
        this.imageTaskSettings = null;
    }

    setIsAuthor() {
        this.isAuthor = setIsAuthor(this.userId);
    }

    authorEffects() {
        if (!this.isAuthor) {
            document.getElementById("submitAuthorButton").classList.add("hide");
            document.getElementById("createAuthorQButton").classList.add("hide");
        } else {
            let destroy = document.getElementById("nextQuestionButton").classList.item(0);
            document.getElementById("nextQuestionButton").classList.remove(destroy);
            document.getElementById("nextQuestionButton").classList.add("hide");
            document.getElementById("scoreTag").innerText = "";
            disableElement(document.getElementById("createAuthorQButton"));
        }
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
            let element = imageTaskHTML(this.imageTaskDisplay);
            document.getElementById('imageTaskArea').appendChild(element);

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

    authorSubmitFinal() {
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

    enterAuthorReview() {
        enableElement(document.getElementById("createAuthorQButton"));
        disableElement(document.getElementById("submitAuthorButton"));
        document.getElementById('imageTaskArea').innerHTML = " ";
        let listOfImageTasks = readJson("/api/authoredQuestions");
        enterAuthorReview(listOfImageTasks, this.userId, this.imageTaskSettings, this.isAuthor, this.pageSettings);
    }

}

function enterAuthorReview(listOfImageTasks, userId, imageTaskSettings, isAuthor, pageSettings) {
    //document.getElementById("authorReviewSubmitButton").classList.remove("hide");
    //document.getElementById("submitButton").classList.add("hide");

    for (var i = 0; i < listOfImageTasks.length; i++) {
        let current = listOfImageTasks[i];
        let canvasName = "canvas" + i;
        let newImageTask = new ImageTaskDisplay(current, userId, imageTaskSettings, isAuthor, canvasName, pageSettings);
        let element = imageTaskHTML(newImageTask);
        document.getElementById('imageTaskArea').appendChild(element);
        newImageTask.lockInCorrectAnswers();
    }
}

function formatAuthorReviewQuestions(number) {
    let element = document.createElement("div");
    let header = document.createElement("h2");
    header.textContent = "Question Set " + (number + 1);
    element.appendChild(header);
    document.getElementById("questionSet").appendChild(element);
}

function imageTaskHTML(imageTaskDisplayObject) {
    let outerNode = document.createElement('div');
    outerNode.classList.add('container-fluid');
    let spaceNode = document.createElement('br');
    outerNode.append(spaceNode);
    outerNode.appendChild(imageTaskDisplayObject.createImageTaskElement());
    return outerNode;
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
