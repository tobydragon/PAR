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

        console.log(imageTaskElement);

        expect(imageTaskElement.getAttribute('class')).toContain('container-fluid');
        expect(imageTaskElement.childNodes.item(1).getAttribute('class')).toContain('row');

        expect(imageTaskElement.childNodes.item(1).childNodes.item(0).getAttribute('class')).toContain('col-1');
        expect(imageTaskElement.childNodes.item(1).childNodes.item(1).getAttribute('class')).toContain('col-6 imgCenter');
        expect(imageTaskElement.childNodes.item(1).childNodes.item(1).childNodes.item(0).getAttribute('id')).toContain('canvasArea');
        expect(imageTaskElement.childNodes.item(1).childNodes.item(1).childNodes.item(0).childNodes.item(0).getAttribute('class')).toContain('canvas');


        expect(imageTaskElement.childNodes.item(1).childNodes.item(2).getAttribute('class')).toContain('col-4');


    });
});
