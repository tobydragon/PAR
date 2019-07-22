class PageDisplay {

    constructor(pageSettings) {
        this.imageTaskDisplay = null;
        this.userId = null;
        //settings
        this.scoreType = pageSettings.scoreType;
        this.showScore = pageSettings.showScore;

    }

    setIsAuthor() {
        this.isAuthor = setIsAuthor(this.userId);
    }

    nextImageTask() {
        var settings;

        try {
            settings = readJson("api/getImageTaskSettings?userId=" + this.userId);
            if (this.isAuthor) {
                var imageTaskJSON = readJson("api/nextAuthorImageTask?userId=" + this.userId);
            } else {
                var imageTaskJSON = readJson("api/nextImageTask?userId=" + this.userId);
            }
            this.imageTaskDisplay = new ImageTaskDisplay(imageTaskJSON, this.userId, settings, this.isAuthor);

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

function setIsAuthor(userId) {
    if (userId === "author") {
        return true;
    } else {
        return false;
    }
}
