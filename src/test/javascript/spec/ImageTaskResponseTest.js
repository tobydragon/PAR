'use strict';

describe("ImageTaskResponse", function () {
    it("makeImageTaskResponse", function(){
        let oldStyleResponse = {
            userId: "Bob",
            questionIds: ["1", "67", "anything"],
            responseTexts: ["answer to 1", "67 answer", "anything Answer"]
        };

        let oopResponse = new ImageTaskResponse(oldStyleResponse);
        expect(oopResponse.constructor.name).toBe("ImageTaskResponse");
        expect(oopResponse.questionResponses.length).toBe(3);
        expect(oopResponse.questionResponses[0].questionId).toBe("1");
        expect(oopResponse.questionResponses[0].answerText).toBe("answer to 1");
        expect(oopResponse.questionResponses[0].hasOwnProperty("questionText")).toBe(false);
    });
});
