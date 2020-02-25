'use strict';

describe("ImageTaskResponse", function () {
    it("makeImageTaskResponse", function(){
        let oldStyleResponse = {
            userId: "Bob",
            questionIds: ["1", "67", "anything"],
            responseTexts: ["answer to 1", "67 answer", "anything Answer"]
        };
        let userID="Bob";
        let QRobj1=new QuestionResponse("1","answer to 1");
        let QRobj2= new QuestionResponse("67","67 answer");
        let QRobj3= new QuestionResponse("anything","anything Answer");
        let responseList=[QRobj1,QRobj2,QRobj3];

        let oopResponse = new ImageTaskResponse(userID,responseList);
        expect(oopResponse.constructor.name).toBe("ImageTaskResponse");
        expect(oopResponse.questionResponses.length).toBe(3);
        expect(oopResponse.questionResponses[0].questionId).toBe("1");
        expect(oopResponse.questionResponses[0].responseText).toBe("answer to 1");
        expect(oopResponse.questionResponses[0].hasOwnProperty("questionText")).toBe(false);
    });
});
