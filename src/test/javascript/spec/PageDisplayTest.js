describe("PageDisplay", function () {
    it("createImageTaskElement", function () {
        let listOfImageTasks = readJson("../resources/author/SampleListOfImageTasks.json");
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
        let imageTaskDisplayObject = new ImageTaskDisplay(listOfImageTasks[0], "testUser", imageTaskSettings, true, "canvas0", pageDisplaySettings);
        let pageDisplayObject = new PageDisplay(pageDisplaySettings);
        let imageTaskElement = imageTaskHTML(imageTaskDisplayObject);

        expect(imageTaskElement.getAttribute('class')).toContain('container-fluid');
        expect(imageTaskElement.childNodes.item(1).getAttribute('class')).toContain('row');
    });
    it("createPageDisplay", function(){
        let listOfImageTasks = readJson("../resources/author/SampleListOfImageTasks.json");
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
        let imageTaskDisplayObject = new ImageTaskDisplay(listOfImageTasks[0], "testUser", imageTaskSettings, true, "canvas0", pageDisplaySettings);
        let pageDisplayObject=new PageDisplay(pageDisplaySettings);
        let createPageDisplay=pageDisplayObject.createPageDisplay();
        expect(createPageDisplay).not.toBe(null);

    });

});
