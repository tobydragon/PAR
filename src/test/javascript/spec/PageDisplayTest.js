describe("PageDisplay", function () {
    it("createImageTask", function () {
        let questionObjects = readJson("../resources/author/DemoQuestionPoolFollowup.json");
        let imageTaskSettings = {
            unsureShowsCorrectAnswer: "False",
            feedbackByType: {
                "plane": "Look at chapter 12",
                structure: "View notes from 9/25",
                attachment: "Read Page 394",
                zone: "Rewatch <a href=https://www.youtube.com/watch?v=dQw4w9WgXcQ> this video </a>"
            },
            ableToResubmitAnswers: "True",
            mustSubmitAnswersToContinue: "False",
            canGiveNoAnswer: "True",
            willDisplayFeedback: "False"
        };
        let pageDisplaySettings = {
            "showScore": false,
            "scoreType": 'NumberByType'
        };
        let imageTaskDisplayObject = new ImageTaskDisplay(questionObjects[0], "testUser", imageTaskSettings, true, "canvas0", pageDisplaySettings);
        let pageDisplayObject = new PageDisplay(pageDisplaySettings);
        let imageTaskElement = reviewMode(imageTaskDisplayObject);
        expect(imageTaskElement).toBe();
    });
});
