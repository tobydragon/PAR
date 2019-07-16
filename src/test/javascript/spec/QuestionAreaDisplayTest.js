'use strict';

describe("QuestionAreaDisplay", function () {
    it("buildQuestionAreaElement", function () {
        let element = buildQuestionAreaElement("ao", "Is this a question?", document.createElement("text"));
        expect(element.tagName.toLowerCase()).toBe("div");
        expect(element.getAttribute("id")).toBe("ao");
        expect(element.childNodes.length).toBe(2);
        expect(element.childNodes.item(0).textContent).toBe("Is this a question?");
    });

    it("buildQuestionAreas", function () {
        let questionObjects = readJson("../resources/author/DemoQuestionPool.json");
        let testResponse = new Response("tester");
        let questionAreas = buildQuestionAreas(questionObjects, testResponse);
        expect(questionAreas.length).toBe(47);
        expect(questionAreas[0].element.getAttribute("id")).toBe("plane./images/demoEquine14.jpg");
        expect(questionAreas[46].element.getAttribute("id")).toBe("zone./images/demoEquine32.jpg");
    });

    it("addFollowupQuestions", function () {
        let questionObjects = readJson("../resources/author/DemoQuestionPoolFollowup.json");
        let testResponse = new Response("tester");
        let questionAreas = buildQuestionAreas(questionObjects, testResponse);
        let questionWithFollowup = questionAreas[1];
        expect(questionWithFollowup.element.childNodes.length).toBe(2);
        questionWithFollowup.addFollowupQuestions();
        //should be one new child holding all the followups
        expect(questionWithFollowup.element.childNodes.length).toBe(3);
        //there should be 3 followups
        expect(questionWithFollowup.element.childNodes[2].childNodes.length).toBe(3);
        //the correct followup id on the first
        expect(questionWithFollowup.element.childNodes[2].childNodes[0].id).toBe("AttachQ1");

        //no followups, shouldn't add the followup element at all
        let questionNoFollowup = questionAreas[0];
        expect(questionNoFollowup.element.childNodes.length).toBe(2);
        questionNoFollowup.addFollowupQuestions();
        expect(questionNoFollowup.element.childNodes.length).toBe(2);

        //test one without followup field defined
        questionObjects = readJson("../resources/author/DemoQuestionPool.json");
        questionNoFollowup = questionAreas[0];
        expect(questionNoFollowup.element.childNodes.length).toBe(2);
        questionNoFollowup.addFollowupQuestions();
        expect(questionNoFollowup.element.childNodes.length).toBe(2);
    });
});
