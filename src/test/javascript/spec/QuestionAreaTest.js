'use strict';

describe("QuestionArea", function () {
    it("buildQuestionAreaElement", function () {
        let element = buildQuestionAreaElement("ao", "Is this a question?", document.createElement("text"));
        expect(element.tagName.toLowerCase()).toBe("div");
        expect(element.getAttribute("id")).toBe("ao");
        expect(element.childNodes.length).toBe(2);
        expect(element.childNodes.item(0).textContent).toBe("Is this a question?");
    });

});