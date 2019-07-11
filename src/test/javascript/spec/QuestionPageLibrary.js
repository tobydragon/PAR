describe("QuestionPageLibraryTest", function () {
    it("check to make sure createDatalistDropdown creates a functioning datalist dropdown question", function () {
        var test = {
            "id": "testType0./images/demoTest01.jpg",
            "type": "testType",
            "questionText": "What is the the square root of -1?",
            "correctAnswer": "testAnswer2",
            "possibleAnswers": ["testAnswer1", "testAnswer2"],
            "imageUrl": "./images/demoTest01.jpg"
        };
        expect(createDatalistDropdown(test, 0)).toBe('<p>What is the the square root of -1?</p> <input id="q0" list="list0"/> <datalist id="list0"><option id= "option0" value="testAnswer1"/><option id= "option1" value="testAnswer2"/><option id= "optionUnsure" value="unsure"/></datalist><i id="questionCorrect0"></i>');
    });
    it("check to make sure createSelectDropdown creates a functioning select dropdown question", function () {
        var test = {
            "id": "testType0./images/demoTest01.jpg",
            "type": "testType",
            "questionText": "What is the the square root of -1?",
            "correctAnswer": "testAnswer2",
            "possibleAnswers": ["testAnswer1", "testAnswer2"],
            "imageUrl": "./images/demoTest01.jpg"
        };
        expect(createSelectDropdown(test, 0)).toBe('<p>What is the the square root of -1?</p> <select id="q0"><option id="option0">testAnswer1</option><option id="option1">testAnswer2</option><option id="optionUnsure">unsure</option></select><i id="questionCorrect0"></i>');
    });
    /**
        it("check to make sure createFillin makes a good attachment question", function () {
            var test = {
                "id": "structure1./images/demoEquine02.jpg",
                "type": "structure",
                "questionText": "What is the the hypoechoic structure?",
                "correctAnswer": "Suspensory ligament (branches)",
                "possibleAnswers": ["Superficial digital flexor tendon", "Deep digital flexor tendon", "Suspensory ligament (body)", "Suspensory ligament (branches)", "Distal check ligament (We use Accessory ligament of the deep digital flexor tendon)", "Metacarpus bone 3 (Third metacarpal bone)", "Proximal sesamoid bones", "P1 (First phalanx)", "P2 (Second phalanx)", "distal sesamoidean ligaments – straight and oblique)", "Palmar annular ligament", "Palmar ligament", "Palmar vessels (medial/lateral)", "Palmar metacarpal vessels (medial/lateral)"],
                "imageUrl": "./images/demoEquine02.jpg"
            };
            expect(createFillIn(test)).toBe('<p>Based on this structure, what is the distal attachment?</p> <input name="q0" list="list0"/> <datalist id="list0"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/><option value="Unsure"/></datalist><i id="questionCorrect0"></i>');
        }); **/
    it("displayCheck to return correct feedback given a value", function () {
        expect(displayCheck("Correct")).toBe("<font color=\"green\">Your answer is: Correct</font>");
        expect(displayCheck("Incorrect")).toBe("<font color=\"red\">Your answer is: Incorrect</font>");
        expect(displayCheck("Unsure", "something", false)).toBe('<font color="#663399">Your answer is: Unsure</font>');
        expect(displayCheck("Unsure", "something", true)).toBe('<font color="#663399">The correct answer is something</font>');
    });
    it("disableField to correctly enable or disable class field of element", function () {
        var testElementDiv = document.createElement("BUTTON");
        var testElementId = testElementDiv.id;
        testElementId = "testElement";
        expect(disableField(testElementId)).toBe(testElementDiv.disabled === true);
    });
    /** it("generateScoreStringByType to generate the correct string for the visualization", function () {
        expect(generateScoreStringByType().toBe("blip"));
    }); **/
    it("compareAnswers to give correct string", function () {
        expect(compareAnswers("bob", "bob", "")).toBe("Correct");
        expect(compareAnswers("bob", "rick", "")).toBe("Incorrect");
        expect(compareAnswers("bob", "unsure", "")).toBe("Unsure");
        expect(compareAnswers("Bob", "bob", "")).toBe("Correct"); //case check on correct answer
        expect(compareAnswers("bob", "Bob", "")).toBe("Correct"); //case check on user answer
        expect(compareAnswers("bob", "", "")).toBe("blank");
        expect(compareAnswers("bob", "Rick", "")).toBe("Incorrect");

    });
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
                "correctAnswer": "Type2",
                "possibleAnswers": ["Type1", "Type2", "Type3"],
                "imageUrl": "./images/demoTest01.jpg",
                "followupQuestions": []
    }]
        }
        expect(generateFollowupQuestions(test)).toBe('<p>Which attachment is this?</p> <input id="q0" list="list0"/> <datalist id="list0"><option id= "option0" value="Type1"/><option id= "option1" value="Type2"/><option id= "option2" value="Type3"/><option id= "optionUnsure" value="unsure"/></datalist><i id="questionCorrect0"></i>');
    });
});
