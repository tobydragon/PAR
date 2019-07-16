class PageDisplay{

    constructor(pageSettings){
        this.imageTaskDisplay= null;
        this.userId= null;
        this.scoreType = pageSettings.scoreType;
        this.showScore = pageSettings.showScore;
    }

    displayUserId(){
        document.getElementById("UserId").innerHTML=this.userId;
    }

    generateScore(){
        let visJSON = readJson("api/getScoreStringByType?userId=" + this.userId);
        return setCurrentScore(visJSON);
    }

    displayScore(given){
        document.getElementById("score").innerHTML= given;
    }

    nextImageTask(){
        var settings;
        
        try {
            settings = readJson("api/getImageTaskSettings");
            var imageTaskJSON = readJson("api/nextImageTask?userId=" + this.userId);
            this.imageTaskDisplay= new ImageTaskDisplay(imageTaskJSON, this.userId, settings);

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
}

function logout() {
    return location.replace('/login');
}
