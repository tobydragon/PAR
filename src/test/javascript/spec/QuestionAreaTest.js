'use strict';

describe("QuestionArea", function () {
    it("buildQuestionAreaElement", function () {
        let element = buildQuestionAreaElement("ao", "Is this a question?", document.createElement("text"));
        expect(element.tagName.toLowerCase()).toBe("div");
        expect(element.getAttribute("id")).toBe("ao");
        expect(element.childNodes.length).toBe(2);
        expect(element.childNodes.item(0).textContent).toBe("Is this a question?");
    });

    it("buildQuestionAreas", function () {
        let questionObjects = readJson("../resources/author/DemoQuestionPool.json");
        let questionAreas = buildQuestionAreas(questionObjects);
        expect(questionAreas.length).toBe(47);
        expect(questionAreas[0].element.getAttribute("id")).toBe("plane./images/demoEquine14.jpg");
        expect(questionAreas[46].element.getAttribute("id")).toBe("zone./images/demoEquine32.jpg");
    });

});