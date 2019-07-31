'use strict';

describe("ImageTaskDisplay", function () {
    it("checkIfShouldAddFollowupQ", function () {
        expect(checkIfShouldAddFollowupQ("correct")).toBe(true);
        expect(checkIfShouldAddFollowupQ("incorrect")).toBe(false);
        expect(checkIfShouldAddFollowupQ("unsure")).toBe(false);
        expect(checkIfShouldAddFollowupQ("")).toBe(false);
        expect(checkIfShouldAddFollowupQ("Correct")).toBe(false);
        expect(checkIfShouldAddFollowupQ("true")).toBe(false);
    });

    it("addToResponseIds", function () {
        let test1 = new Response("test");
        addToResponseIds(test1, "\"zone./images/demoEquine14.jpg\"");
        expect(test1.taskQuestionIds.length).toBe(1);
        expect(test1.taskQuestionIds).toEqual(['"zone./images/demoEquine14.jpg"']);

        addToResponseIds(test1, "\"zone./images/demoEquine14.jpg\"");
        expect(test1.taskQuestionIds.length).toBe(1);
        expect(test1.taskQuestionIds).toEqual(['"zone./images/demoEquine14.jpg"']);

        addToResponseIds(test1, "\"plane./images/demoEquine02.jpg\"");
        expect(test1.taskQuestionIds.length).toBe(2);
        expect(test1.taskQuestionIds).toEqual(['"zone./images/demoEquine14.jpg"', '"plane./images/demoEquine02.jpg"']);

        addToResponseIds(test1, "\"plane./images/demoEquine02.jpg\"");
        expect(test1.taskQuestionIds.length).toBe(2);
        expect(test1.taskQuestionIds).toEqual(['"zone./images/demoEquine14.jpg"', '"plane./images/demoEquine02.jpg"']);

        addToResponseIds(test1, "\"BonusQ2\"");
        expect(test1.taskQuestionIds.length).toBe(3);
        expect(test1.taskQuestionIds).toEqual(['"zone./images/demoEquine14.jpg"', '"plane./images/demoEquine02.jpg"', '"BonusQ2"']);
    });

    it("giveFeedback", function () {
        let feedbackByType = {
            plane: "Look at chapter 12",
            structure: "View notes from 9/25",
            attachment: "Read Page 394",
            zone: "Rewatch <a href=https://www.youtube.com/watch?v=dQw4w9WgXcQ> this video </a>"
        };
        let test1 = ["plane"];
        let test2 = ["plane", "structure"];
        let test3 = ["plane", "structure", "attachment"];
        let test4 = ["plane", "structure", "attachment", "zone"];
        expect(giveFeedback(test1, feedbackByType)).toBe("Feedback: Look at chapter 12");
        expect(giveFeedback(test2, feedbackByType)).toBe("Feedback: Look at chapter 12, View notes from 9/25");
        expect(giveFeedback(test3, feedbackByType)).toBe("Feedback: Look at chapter 12, View notes from 9/25, Read Page 394");
        expect(giveFeedback(test4, feedbackByType)).toBe("Feedback: Look at chapter 12, View notes from 9/25, Read Page 394, Rewatch <a href=https://www.youtube.com/watch?v=dQw4w9WgXcQ> this video </a>");

    });

    it("checkIfCanContinu", function () {
        let test1 = ["Correct", "Incorrect", "Incorrect", "Incorrect", "Correct"];
        let test2 = ["Correct", "Incorrect", "Incorrect", "Incorrect", "Correct", ""];
        let test3 = ["Correct", "Incorrect", "", "", "Incorrect", "Incorrect", "Correct"];
        expect(checkIfCanContinu(true, test1)).toBe(true);
        expect(checkIfCanContinu(true, test2)).toBe(true);
        expect(checkIfCanContinu(true, test3)).toBe(true);

        expect(checkIfCanContinu(false, test1)).toBe(true);
        expect(checkIfCanContinu(false, test2)).toBe(false);
        expect(checkIfCanContinu(false, test3)).toBe(false);
    });
    it("addUnsureToAnswers", function () {
        let questionObjects = readJson("../resources/author/DemoQuestionPoolFollowup.json");
        addUnsureToAnswers(questionObjects);

        expect(questionObjects[0].possibleAnswers).toContain(ResponseResult.unsure);
    });


    it('createQuestionAreaElement Test', function () {
        let listOfImageTasks = readJson("../resources/author/SampleListOfImageTasks.json");
        let imageTaskSettings = readJson("../../main/resources/author/SettingsExample.json");
        let pageSettings = readJson("../../main/resources/author/PageSettingsExample.json");

        let imageTaskDisplayObject = new ImageTaskDisplay(listOfImageTasks[0], "testUser", imageTaskSettings, true, "canvas0", pageSettings);

        let questionAreaElement = imageTaskDisplayObject.createQuestionAreaElement();

        expect(questionAreaElement.getAttribute('id')).toBe('questionSetForm');
        expect(questionAreaElement.childNodes.item(0).getAttribute('id')).toBe('questionSet');
        expect(questionAreaElement.childNodes.item(0).childElementCount).toBe(3);
        expect(questionAreaElement.childNodes.item(0).childNodes.item(0).getAttribute('id')).toBe('PlaneQ1');
    });

    it("createCanvasElement Test", function () {
        let listOfImageTasks = readJson("../resources/author/SampleListOfImageTasks.json");
        let imageTaskSettings = readJson("../../main/resources/author/SettingsExample.json");
        let pageSettings = readJson("../../main/resources/author/PageSettingsExample.json");

        let imageTaskDisplayObject = new ImageTaskDisplay(listOfImageTasks[0], "testUser", imageTaskSettings, true, "canvas0", pageSettings);

        let canvasTestElement = imageTaskDisplayObject.createCanvasElement();

        expect(canvasTestElement.getAttribute('id')).toContain('canvasArea');
        expect(canvasTestElement.childNodes.item(0).getAttribute('id')).toBe('canvas0');
        expect(canvasTestElement.childNodes.item(0).getAttribute('class')).toBe('canvas');
    });

    it('createSubmitButtonElement Test', function () {
        let listOfImageTasks = readJson("../resources/author/SampleListOfImageTasks.json");
        let imageTaskSettings = readJson("../../main/resources/author/SettingsExample.json");
        let pageSettings = readJson("../../main/resources/author/PageSettingsExample.json");

        let imageTaskDisplayObject = new ImageTaskDisplay(listOfImageTasks[0], "testUser", imageTaskSettings, true, "canvas0", pageSettings);

        let submitButtonTestElement = imageTaskDisplayObject.createSubmitButtonElement();

        expect(submitButtonTestElement.getAttribute('class')).toContain('row');
        expect(submitButtonTestElement.childNodes.item(0).getAttribute('class')).toContain('col-12 text-center');
        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(0).getAttribute('class')).toContain('show');

        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(0).childNodes.item(0).getAttribute('id')).toContain('submitButtonTag');
        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(0).childNodes.item(0).childNodes.item(0).getAttribute('type')).toContain('button');
        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(0).childNodes.item(0).childNodes.item(1).getAttribute('type')).toContain('button');
        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(0).childNodes.item(0).childNodes.item(1).getAttribute('class')).toContain('fas fa-arrow-circle-right btn btn-outline-dark');

        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(1).childNodes.item(0).getAttribute('class')).toContain('col-12 text-center');
        expect(submitButtonTestElement.childNodes.item(0).childNodes.item(1).childNodes.item(0).childNodes.item(0).getAttribute('id')).toContain('Ids');

    });
    it("createImageTaskElement", function () {
        let listOfImageTasks = readJson("../resources/author/SampleListOfImageTasks.json");
        let imageTaskSettings = readJson("../../main/resources/author/SettingsExample.json");
        let pageSettings = readJson("../../main/resources/author/PageSettingsExample.json");

        let imageTaskDisplayObject = new ImageTaskDisplay(listOfImageTasks[0], "testUser", imageTaskSettings, true, "canvas0", pageSettings);

        let imageTaskElement = imageTaskDisplayObject.createImageTaskElement();

        expect(imageTaskElement.getAttribute('class')).toContain('row');

        expect(imageTaskElement.childNodes.item(0).getAttribute('class')).toContain('col-1');
        expect(imageTaskElement.childNodes.item(1).getAttribute('class')).toContain('col-6 imgCenter');
        expect(imageTaskElement.childNodes.item(1).childNodes.item(0).getAttribute('id')).toContain('canvasArea');
        expect(imageTaskElement.childNodes.item(1).childNodes.item(0).childNodes.item(0).getAttribute('class')).toContain('canvas');

        expect(imageTaskElement.childNodes.item(2).getAttribute('class')).toContain('col-4');

        expect(imageTaskElement.childNodes.item(3).getAttribute('class')).toContain('col-1');
    });
});
