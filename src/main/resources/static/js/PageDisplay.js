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
    createPageDisplay(){
        this.setIsAuthor();
        this.displayUserId();
        this.prepareScore();
        document.getElementById('imageTaskArea').innerText = "";
        this.nextImageTask();
        this.authorEffects();

    }
    setIsAuthor() {
        this.isAuthor = setIsAuthor(this.userId);
    }
    displayUserId() {
        document.getElementById("UserId").innerHTML = "&nbsp" + this.userID;
    }

    authorEffects() {
        if (!this.isAuthor) {
            document.getElementById("submitAuthorButton").classList.add("hide");
            document.getElementById("createAuthorQButton").classList.add("hide");
        } else {
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
            this.imageTaskDisplay = new ImageTaskDisplay(imageTaskJSON, this.userId, this.imageTaskSettings, this.isAuthor, "myCanvas", this.pageSettings, 0);
            //0 used for counter as 1 image task doesnt need an incrementing value
            let element = imageTaskHTML(this.imageTaskDisplay);
            document.getElementById('imageTaskArea').appendChild(element);
            this.imageTaskDisplay.displayImageUrl();

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
        document.getElementById('imageTaskArea').innerText = "";
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
        document.getElementById('imageTaskArea').innerText = "Questions Submitted";
    }

    nextQuestion() {
        if (!this.pageDisplay.imageTaskDisplay.mustSubmitAnswersToContinue) {
            this.nextImageTask();
        } else {
            if (this.pageDisplay.imageTaskDisplay.haveSubmited) {
                this.nextImageTask();
            } else {
                document.getElementById("errorFeedback").innerHTML = "<font color=red>Must submit answers to continue</font>";
            }
        }
    }

    enterAuthorWrite() {
        document.getElementById('imageTaskArea').innerText = "";
        enableElement(document.getElementById("submitAuthorButton"));
        disableElement(document.getElementById("createAuthorQButton"));
        this.nextQuestion();
    }

    enterAuthorReview() {
        enableElement(document.getElementById("createAuthorQButton"));
        disableElement(document.getElementById("submitAuthorButton"));
        document.getElementById('imageTaskArea').innerHTML = " ";
        let listOfImageTasks = readJson("/api/authoredQuestions");
        enterAuthorReview(listOfImageTasks, this.userId, this.imageTaskSettings, this.isAuthor, this.pageSettings);
    }

    prepareScore() {
        if (this.showScore) {
            displayScore(generateScore(this.scoreType, this.userId));
        }
    }
}

function displayScore(given) {
    if (document.getElementById("score").hasChildNodes()) {
        let node = document.getElementById("score").firstChild;
        document.getElementById("score").removeChild(node);
    }
    document.getElementById("score").appendChild(given);
}

function generateScore(scoreType, userID) {
    if (scoreType === "VisualByType") {
        let visJSON = readJson("api/knowledgeBase?userId=" + userID);
        return setCurrentScore(visJSON, scoreType);
    } else if (scoreType === "NumberByType") {
        let scoreJSON = readJson("api/calcScoreByType?userId=" + userID);
        return setCurrentScore(scoreJSON, scoreType);
    }
}

function enterAuthorReview(listOfImageTasks, userId, imageTaskSettings, isAuthor, pageSettings) {
    for (var i = 0; i < listOfImageTasks.length; i++) {
        let current = listOfImageTasks[i];
        let canvasName = "canvas" + i;
        let newImageTask = new ImageTaskDisplay(current, userId, imageTaskSettings, isAuthor, canvasName, pageSettings, i);
        let element = imageTaskHTML(newImageTask);
        document.getElementById('imageTaskArea').appendChild(element);
        newImageTask.displayImageUrl();
        newImageTask.lockInCorrectAnswers();
        document.getElementById('submitButton' + i).classList.add("hide");
        let destroy = document.getElementById('nextQuestionButton' + i).classList.item(0);
        document.getElementById('nextQuestionButton' + i).classList.remove(destroy);
        document.getElementById('nextQuestionButton' + i).classList.add("hide");
    }
    document.getElementById('imageTaskArea').appendChild(createMoveQuestionsToPoolButton());
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

function createMoveQuestionsToPoolButton() {
    let outerMoveButtonNode = document.createElement('div');
    outerMoveButtonNode.classList.add('row');

    let innerMoveButtonNode = document.createElement('div');
    innerMoveButtonNode.classList.add('col-12');
    innerMoveButtonNode.classList.add('text-center');

    let authorButtonElement = document.createElement('button');
    authorButtonElement.setAttribute('type', 'button');
    authorButtonElement.classList.add('btn');
    authorButtonElement.classList.add('btn-primary');
    authorButtonElement.setAttribute('id', 'authorReviewSubmitButton');
    authorButtonElement.setAttribute('onclick', 'completeDisplay.pageDisplay.authorSubmitFinal()');
    authorButtonElement.textContent = 'Add Questions To Pool';

    innerMoveButtonNode.appendChild(authorButtonElement);
    outerMoveButtonNode.appendChild(innerMoveButtonNode);

    let spaceNode = document.createElement('br');
    outerMoveButtonNode.appendChild(spaceNode);
    return outerMoveButtonNode;
}
