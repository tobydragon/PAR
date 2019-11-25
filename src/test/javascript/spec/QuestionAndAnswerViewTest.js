'use strict';

describe("QuestionAndAnswerController", function () {
    it("getResponse", function(){
        let qaModel = {
            id: "qa31",
            questionText:null,
            possibleAnswers: ["transverse", "Both proximal and middle phalanxes"],
            correctAnswer: "transverse"

        };
        let qaavTestObj = new QuestionAndAnswerController(qaModel);
        expect(qaavTestObj.getResponse()).toBeNull();
        qaavTestObj.answerView.inputTextbox.value="transverse";
        qaavTestObj.questionView.element.value="What is this structure?";
        expect(qaavTestObj.getResponse()).not.toBeNull();
    });
    //No test needed for checkAndUpdateView as test functionality already shown in the AnswerViewTest.
    it("buildQuestionAndAnswerViewElement", function(){
        let qaModel = {
            id: "qa31",
            questionText:null,
            possibleAnswers: ["transverse", "Both proximal and middle phalanxes"],
            correctAnswer: "transverse"
        };
        let qaavTestObj = new QuestionAndAnswerController(qaModel);
        let buildQandAViewTestElement=buildQuestionAndAnswerViewElement(qaModel.id,qaavTestObj.questionView.element,qaavTestObj.answerView.inputTextbox);
        expect(buildQandAViewTestElement).not.toBeNull();
        expect(buildQandAViewTestElement.getAttribute('id')).toBe(qaModel.id);
        expect(buildQandAViewTestElement.childNodes.item(0)).toBe(qaavTestObj.questionView.element);
        expect(buildQandAViewTestElement.childNodes.item(1)).toBe(qaavTestObj.answerView.inputTextbox);
        expect(buildQandAViewTestElement.getAttribute('class')).toContain('pad5');
    });
});
