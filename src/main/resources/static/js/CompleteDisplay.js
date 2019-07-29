class CompleteDisplay {

    constructor() {
        this.pageDisplay = null;
        this.pageSettings = null;
        this.userID = null;
    }

    createPageDisplay() {
        try {
            this.pageSettings = readJson("api/getPageSettings?userId=" + this.userID);

        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }

        this.pageSettings = readJson("api/getPageSettings?userId=" + this.userID);
        this.pageDisplay = new PageDisplay(this.pageSettings);
        this.pageDisplay.userId = this.userID;
    }

    setPageDisplayIsAuthor() {
        this.pageDisplay.setIsAuthor();
    }

    displayUserId() {
        document.getElementById("UserId").innerHTML = "&nbsp" + this.userID;
    }

    nextImageTask() {
        document.getElementById("questionSet").innerText = "";
        this.pageDisplay.nextImageTask();
    }

    nextQuestion() {
        document.getElementById("helpfulFeedback").innerHTML = " ";
        document.getElementById("errorFeedback").innerHTML = " ";
        if (!this.pageDisplay.imageTaskDisplay.mustSubmitAnswersToContinue) {
            this.nextImageTask();
        } else {
            if (this.pageDisplay.imageTaskDisplay.haveSubmited) {
                this.nextImageTask();
            } else {
                document.getElementById("errorFeedback").innerHTML = "<font color=red>Must submit answers to continue</font>";
            }
        }
        if(this.pageDisplay.isAuthor){
            document.getElementById("submitButton").classList.remove("hide");
            document.getElementById("authorReviewSubmitButton").classList.add("hide");
        }
    }

    showScoreInner(){
        showScoreOuter(this.pageDisplay.showScore, this.pageDisplay.scoreType, this.userID);
    }
}

function showScoreOuter(showScore, scoreType, userId) {
    if (showScore) {
        displayScore(generateScore(scoreType, userId));
    }
}

function displayScore(given) {
    if (document.getElementById("score").hasChildNodes()) {
        let node = document.getElementById("score").firstChild;
        document.getElementById("score").removeChild(node);
    }
    document.getElementById("score").appendChild(given);
}

function  generateScore(scoreType, userID) {
    if (scoreType === "VisualByType") {
        let visJSON = readJson("api/knowledgeBase?userId=" + userID);
        return setCurrentScore(visJSON, scoreType);
    } else if (scoreType === "NumberByType") {
        let scoreJSON = readJson("api/calcScoreByType?userId=" + userID);
        return setCurrentScore(scoreJSON, scoreType);
    }
}
