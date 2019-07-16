class PageDisplay{

    constructor(){
        this.imageTaskDisplay= null;
        this.userId= null;
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
        try {
            var imageTaskJSON = readJson("api/nextImageTask?userId=" + this.userId);

        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }

        this.imageTaskDisplay= new ImageTaskDisplay(imageTaskJSON, this.userId);
    }

    logout() {
        this.userId = null;
        return location.replace('/login');
    }
}
