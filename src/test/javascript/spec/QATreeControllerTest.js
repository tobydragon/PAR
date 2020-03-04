describe("QATreeController", function(){
    let qaModel = {
        id: "qaTop0",
        questionText:null,
        possibleAnswers: ["right", "wrong", "neither"],
        correctAnswer: "right",
        followupQuestions: [
            {
                id: "q01",
                questionText: "q01",
                possibleAnswers: ["right", "wrong", "neither"],
                correctAnswer: "wrong",
                followupQuestions: []
            },
            {
                id: "q02",
                questionText: "q02",
                possibleAnswers: ["aa", "bb", "cc"],
                correctAnswer: "cc",
                followupQuestions: [
                    {
                        id: "q021",
                        questionText: null,
                        possibleAnswers: ["right", "wrong", "neither"],
                        correctAnswer: "wrong",
                        followupQuestions: []
                    },
                    {
                        id: "q022",
                        questionText: "q022",
                        possibleAnswers: ["aa", "bb", "cc"],
                        correctAnswer: "cc",
                    },
                    {
                        id: "q023",
                        questionText: "q023",
                        possibleAnswers: ["xx", "yy", "zz"],
                        correctAnswer: "xx"
                    }
                ]
            },
            {
                id: "q03",
                questionText: null,
                possibleAnswers: ["xx", "yy", "zz"],
                correctAnswer: "xx"
            }
        ]

    };
    let qatvObj = new QATreeController(qaModel);
    it("showNextLevelFollowupQuestions", function(){
        expect(qatvObj.showNextLevelFollowupQuestions().element.childNodes.item(0)).toContain(buildQATreeListElement(qatvObj.followupQATreeControllers));
    }) ;
    it("areAnswerBoxAndAllFollowupAnswerBoxesDisabled - everything enabled", function(){
       expect(qatvObj.areAnswerBoxAndAllFollowupAnswerBoxesDisabled()).toBe(false);
    });
    it("areAnswerBoxAndAllFollowupAnswerBoxesDisabled - just child disabled", function(){
        qatvObj.qaController.answerController.inputTextbox.disabled = false;
        console.log(qatvObj.followupQATreeControllers);
        qatvObj.followupQATreeControllers[0].qaController.answerController.inputTextbox.disabled = true;
        expect(qatvObj.areAnswerBoxAndAllFollowupAnswerBoxesDisabled()).toBe(false);
    });
    it("areAnswerBoxAndAllFollowupAnswerBoxesDisabled - just grandchild disabled", function(){
        qatvObj.followupQATreeControllers[0].qaController.answerController.inputTextbox.disabled = false;
        qatvObj.followupQATreeControllers[1].followupQATreeControllers[1].qaController.answerController.inputTextbox.disabled = true;
        expect(qatvObj.areAnswerBoxAndAllFollowupAnswerBoxesDisabled()).toBe(false);
    });
    it("areAnswerBoxAndAllFollowupAnswerBoxesDisabled - all disabled", function(){
        qatvObj.qaController.answerController.inputTextbox.disabled = true;
        qatvObj.followupQATreeControllers[0].qaController.answerController.inputTextbox.disabled = true;
        qatvObj.followupQATreeControllers[1].qaController.answerController.inputTextbox.disabled = true;
        qatvObj.followupQATreeControllers[1].followupQATreeControllers[0].qaController.answerController.inputTextbox.disabled = true;
        qatvObj.followupQATreeControllers[1].followupQATreeControllers[1].qaController.answerController.inputTextbox.disabled = true;
        qatvObj.followupQATreeControllers[1].followupQATreeControllers[2].qaController.answerController.inputTextbox.disabled = true;
        qatvObj.followupQATreeControllers[2].qaController.answerController.inputTextbox.disabled = true;

        expect(qatvObj.areAnswerBoxAndAllFollowupAnswerBoxesDisabled()).toBe(true);
    });
    it("areAnswerBoxAndAllFollowupAnswerBoxesDisabled - just 1 grandchild enabled", function(){
        qatvObj.followupQATreeControllers[1].followupQATreeControllers[1].qaController.answerController.inputTextbox.disabled = false;
        expect(qatvObj.areAnswerBoxAndAllFollowupAnswerBoxesDisabled()).toBe(false);
    });

    it("buildQAViewTreeElement", function(){
    let testElement=buildQATreeViewElement(qatvObj.id+"QATreeView", qatvObj.qaController.element);
    expect(testElement.getAttribute("id")).toBe(qatvObj.id+"QATreeView");
    expect(testElement.childNodes.item(0)).toBe(qatvObj.qaController.element);
    });
});