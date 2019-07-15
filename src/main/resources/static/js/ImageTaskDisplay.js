class ImageTaskDisplay{
    #questionAreadiplay;
    #pageImage;
    #response;
    #userId;

    constructor(imageTaskJson, userId){
        this.#userId=userId;
        this.#response= new Response(userId);
        this.#pageImage= new PageImage(imageTaskJson.imageUrl);
        this.#questionAreadiplay= new buildQuestionAreas(imageTaskJson.taskQuestions, this.#response);
    }

    submitAnswers(){
        for(var i=0; i<this.#questionAreadiplay.length; i++){
            this.#questionAreadiplay[i].answerBox.checkCurrentResponse(this.#response);
        }

        submitToAPI("api/recordResponse", this.#response);
    }
}
