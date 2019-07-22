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
            this.pageDisplay.displayScore(this.pageDisplay.generateScore());
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
    
}