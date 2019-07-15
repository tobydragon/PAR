class PageDisplay{

    constructor(){
        this.imageTaskDisplay= null;
        this.userId= null;
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