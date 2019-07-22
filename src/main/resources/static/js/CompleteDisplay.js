class CompleteDisplay {

    constructor() {
        this.pageDisplay=null;
        this.pageSettings=null;
        this.userID= null;
    }

    createPageDisplay(){
        try {
            this.pageSettings = readJson("api/getPageSettings?userId=" + this.userID);

        } catch (Exception) {
            window.onerror = function(msg) {
                location.replace('/error?message=' + msg);
            }
        }

        this.pageSettings = readJson("api/getPageSettings?userId=" + this.userID);
        this.pageDisplay = new PageDisplay(this.pageSettings);
        this.pageDisplay.userId = this.userID;
        this.pageDisplay.setIsAuthor();
    }

    showScore(){
        if (this.pageDisplay.showScore) {
            this.displayScore(this.generateScore());
        }
    }

    nextImageTask(){
        document.getElementById("questionSet").innerText="";
        if (this.pageDisplay.isAuthor) {
            this.pageDisplay.nextAuthorImageTask();
        } else {
            this.pageDisplay.nextImageTask();
        }
    }

    displayUserId() {
        document.getElementById("UserId").innerHTML = "&nbsp" + this.userID;
        console.log("Excuse?");
        console.log(this.userID);
    }

    generateScore() {
        let visJSON = readJson("api/knowledgeBase?userId=" + this.userID);
        return setCurrentScore(visJSON, this.pageDisplay.scoreType);
    }

    displayScore(given) {
        if(document.getElementById("score").hasChildNodes()) {
            let node=document.getElementById("score").firstChild;
            document.getElementById("score").removeChild(node);
        }
        document.getElementById("score").appendChild(given);
    }

    nextQuestion(){
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