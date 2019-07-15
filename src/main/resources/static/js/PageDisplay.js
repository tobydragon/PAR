class PageDisplay{
    #imageTaskDisplay;
    userId;

    nextImageTask(){
        try {
            var imageTaskJSON = readJson("api/nextImageTask?userId=" + sendUserId());

        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }

        this.#imageTaskDisplay= new ImageTaskDisplay(imageTaskJSON, this.userId);
    }
}