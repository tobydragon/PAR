describe("QuestionPageLibraryTest", function () {
    it("check to make sure createFillin makes a good structure question", function () {
        var test = {
            id: "structureQ1",
            difficulty: 2,
            questionText: "What structure is in the near field?",
            correctAnswer: "bone",
            possibleAnswers: ["bone", "ligament", "tumor", "tendon", "Unsure"],
            imageUrl: "../static/images/demoEquine02.jpg"
        };
        expect(createFillIn(test)).toBe('<p>What structure is in the near field?</p> <input name="q0" list="list0"/> <datalist id="list0"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/><option value="Unsure"/></datalist><i id="questionCorrect0"></i>');
    });

    it("check to make sure createFillin makes a good attachment question", function () {
        var test = {
            id: "attachmentQ1",
            difficulty: 3,
            questionText: "Based on this structure, what is the distal attachment?",
            correctAnswer: "tendon",
            possibleAnswers: ["bone", "ligament", "tumor", "tendon", "Unsure"],
            imageUrl: "../static/images/demoEquine02.jpg"
        };
        expect(createFillIn(test)).toBe('<p>Based on this structure, what is the distal attachment?</p> <input name="q0" list="list0"/> <datalist id="list0"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/><option value="Unsure"/></datalist><i id="questionCorrect0"></i>');
    });
    it("displayCheck to return correct feedback given a value", function () {
        expect(displayCheck("Correct")).toBe("<font color=\"green\">Your answer is: Correct</font>");
        expect(displayCheck("Incorrect")).toBe("<font color=\"red\">Your answer is: Incorrect</font>");
        expect(displayCheck("Unsure", "something" , false)).toBe('<font color="#663399">Your answer is: Unsure</font>');
        expect(displayCheck("Unsure", "something", true)).toBe('<font color="#663399">Your answer is: Unsure.    The answer is something</font>');
    });
});
