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
        if(qatvObj.hasOwnProperty("followUpQuestions")) {
            console.log(qatvObj.showNextLevelFollowupQuestions());
            expect(qatvObj.showNextLevelFollowupQuestions().element.childNodes.item(0)).toContain(buildQATreeListElement(qatvObj.followupQATreeViews));
        }
   }) ;
    it("checkAnswersAndUpdateView", function(){

    });
    it("buildQATreeViewElement", function(){
    let testElement=buildQATreeViewElement(qatvObj.id+"QATreeView", qatvObj.qaView.element);
    expect(testElement.getAttribute("id")).toBe(qatvObj.id+"QATreeView");
    expect(testElement.childNodes.item(0)).toBe(qatvObj.qaView.element);
    });
    it("buildQATreeViewList", function(){

    });
});