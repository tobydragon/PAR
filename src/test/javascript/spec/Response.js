'use strict';

describe("Response", function () {
    it("constructor", function () {
        let test= new Response("test1");
    });

    it("addToResponseTexts", function () {
        let test= new Response("test1");
        test.addToResponseTexts("longitudinal");
        expect(test.responseTexts.length).toBe(1);
    });

    it(" addToQuestionIds", function () {
        let test= new Response("test1");
        test.addToQuestionIds("stuff")
        expect(test.taskQuestionIds.length).toBe(1);
    });
});