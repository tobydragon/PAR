'use strict';

describe("QuestionAndAnswerController", function () {
    it("getResponse", function(){
        //first check to be null
        let qaModel0 = {
            id: "qa31",
            questionText:null,
            possibleAnswers: ["transverse", "Both proximal and middle phalanxes"],
            correctAnswer: "transverse"
        };
        let qaavTestObj = new QuestionAndAnswerController(qaModel0);
        expect(qaavTestObj.getResponse()).toBeNull();

        //second check to not be null
        let qaModel1 = {
            id: "qa31",
            questionText:"What is this structure",
            possibleAnswers: ["transverse", "Both proximal and middle phalanxes"],
            correctAnswer: "transverse"
        };
        let qaavTestObj2=new QuestionAndAnswerController(qaModel1);
        qaavTestObj2.answerController.inputTextbox.value="transverse";
        expect(qaavTestObj2.getResponse()).not.toBeNull();
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
        let buildQandAViewTestElement=buildQuestionAndAnswerViewElement(qaModel.id,qaavTestObj.questionController.element,qaavTestObj.answerController.inputTextbox);
        expect(buildQandAViewTestElement).not.toBeNull();
        expect(buildQandAViewTestElement.getAttribute('id')).toBe(qaModel.id);
        expect(buildQandAViewTestElement.childNodes.item(0)).toBe(qaavTestObj.questionController.element);
        expect(buildQandAViewTestElement.childNodes.item(1)).toBe(qaavTestObj.answerController.inputTextbox);
        expect(buildQandAViewTestElement.getAttribute('class')).toContain('pad5');
    });
});
