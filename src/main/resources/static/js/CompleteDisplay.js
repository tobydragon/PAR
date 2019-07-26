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

    showScore() {
        if (this.pageDisplay.showScore) {
            this.displayScore(this.generateScore());
        }
    }

    generateScore() {
        if (this.pageDisplay.scoreType === "VisualByType") {
            let visJSON = readJson("api/knowledgeBase?userId=" + this.userID);
            return setCurrentScore(visJSON, this.pageDisplay.scoreType);
        } else if (this.pageDisplay.scoreType === "NumberByType") {
            let scoreJSON = readJson("api/calcScoreByType?userId=" + this.userID);
            return setCurrentScore(scoreJSON, this.pageDisplay.scoreType);
        }
    }

    displayScore(given) {
        if (document.getElementById("score").hasChildNodes()) {
            let node = document.getElementById("score").firstChild;
            document.getElementById("score").removeChild(node);
        }
        document.getElementById("score").appendChild(given);
    }

    nextImageTask() {
        document.getElementById("questionSet").innerText = "";
        this.pageDisplay.nextImageTask();
    }

    nextQuestion() {
        document.getElementById("errorFeedback").innerHTML = " ";
        if (!this.pageDisplay.imageTaskDisplay.mustSubmitAnswersToContinue) {
            this.nextImageTask();
            this.showScore();
        } else {
            if (this.pageDisplay.imageTaskDisplay.haveSubmited) {
                this.nextImageTask();
                this.showScore();
            } else {
                document.getElementById("errorFeedback").innerHTML = "<font color=red>Must submit answers to continue</font>";
            }
        }
    }

}