'use strict';

describe("QuestionAndAnswerView", function () {
    it("getResponse", function(){
        let qaModel = {
            id: "qa31",
            questionText:null,
            possibleAnswers: ["transverse", "Both proximal and middle phalanxes"],
            correctAnswer: "transverse"

        };
        let qaavTestObj = new QuestionAndAnswerView(qaModel);
        expect(qaavTestObj.getResponse()).toBeNull();
        qaavTestObj.answerView.inputTextbox.value="transverse";
        qaavTestObj.questionView.element.value="What is this structure?";
        expect(qaavTestObj.getResponse()).not.toBeNull();
    });

});
