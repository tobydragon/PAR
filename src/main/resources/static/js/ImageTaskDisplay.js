class ImageTaskDisplay{
    #questionAreadiplay;
    #pageImage;
    #response;
    #userId;

    constructor(imageTaskJson, userId){
        this.#userId=userId;
        this.#response= new Response(userId);
        this.#pageImage= new PageImage(imageTaskJson.imageUrl);
        this.#questionAreadiplay= new QuestionAreaDisplay(imageTaskJson.taskQuestions);
    }

    submitAnswers(){
        this.#questionAreadiplay.
    }
}
