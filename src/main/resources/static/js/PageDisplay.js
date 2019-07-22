class PageDisplay {

    constructor(pageSettings) {
        this.imageTaskDisplay = null;
        this.userId = null;
        //settings
        this.scoreType = pageSettings.scoreType;
        this.showScore = pageSettings.showScore;

    }

    setIsAuthor() {
        if (this.userId === "author") {
            this.isAuthor = true;
        } else {
            this.isAuthor = false;
        }
    }

    nextAuthorImageTask() {
        var settings;

        //TODO: Needs a new URL
        try {
            settings = readJson("api/getImageTaskSettings?userId=" + this.userId);
            var imageTaskJSON = readJson("api/nextImageTask?userId=" + this.userId);
            this.imageTaskDisplay = new ImageTaskDisplay(imageTaskJSON, this.userId, settings, this.isAuthor);

        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }
    }

    nextImageTask() {
        var settings;

        try {
            settings = readJson("api/getImageTaskSettings?userId=" + this.userId);
            var imageTaskJSON = readJson("api/nextImageTask?userId=" + this.userId);
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
