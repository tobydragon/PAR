describe("QuestionPage", function () {
    it("check to make sure generateFollowupQuestions create appropriate followup questions for a correct struct question", function () {
        var test = {
            "id": "structure1./images/demoTest01.jpg",
            "type": "structure",
            "questionText": "What is my answer?",
            "correctAnswer": "testAnswer1",
            "possibleAnswers": ["testAnswer1", "testAnswer2"],
            "imageUrl": "./images/demoTest01.jpg",
            "followupQuestions": [{
                "id": "AttachQ4",
                "type": "Attachment",
                "questionText": "Which attachment is this?",
                "correctAnswer": "TypeTwo",
                "possibleAnswers": ["TypeOne", "TypeTwo", "TypeThree"],
                "imageUrl": "./images/demoTest01.jpg",
                "followupQuestions": []
    }]
        }
        expect(generateFollowupQuestions(test)).toBe('<p>Which attachment is this?</p> <input id="q0" list="list0"/> <datalist id="list0"><option id= "option0" value="TypeOne"/><option id= "option1" value="TypeTwo"/><option id= "option2" value="TypeThree"/><option id= "optionUnsure" value="unsure"/></datalist><i id="questionCorrect0"></i>');
    });
});
