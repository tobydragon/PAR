describe("QuestionPageLibraryTest", function () {
    it("check to make sure createFillin makes a good structure question", function () {
        var test = {
            "id": "structure1./images/demoEquine02.jpg",
            "type": "structure",
            "questionText": "What is the the hypoechoic structure?",
            "correctAnswer": "Suspensory ligament (branches)",
            "possibleAnswers": ["Superficial digital flexor tendon", "Deep digital flexor tendon", "Suspensory ligament (body)", "Suspensory ligament (branches)", "Distal check ligament (We use Accessory ligament of the deep digital flexor tendon)", "Metacarpus bone 3 (Third metacarpal bone)", "Proximal sesamoid bones", "P1 (First phalanx)", "P2 (Second phalanx)", "distal sesamoidean ligaments – straight and oblique)", "Palmar annular ligament", "Palmar ligament", "Palmar vessels (medial/lateral)", "Palmar metacarpal vessels (medial/lateral)"],
            "imageUrl": "./images/demoEquine02.jpg"
        };
        expect(createFillIn(test)).toBe('<p>What is the the hypoechoic structure?</p> <input name="q0" list="list0"/> <datalist id="list0"><option value="Superficial digital flexor tendon"/><option value="Deep digital flexor tendon"/><option value="Suspensory ligament (body)"/><option value="Suspensory ligament (branches)"/><option value="Distal check ligament (We use Accessory ligament of the deep digital flexor tendon)"/><option value="Metacarpus bone 3 (Third metacarpal bone)"/><option value="Proximal sesamoid bones"/><option value="P1 (First phalanx)"/><option value="P2 (Second phalanx)"/><option value="distal sesamoidean ligaments – straight and oblique)"/><option value="Palmar annular ligament"/><option value="Palmar ligament"/><option value="Palmar vessels (medial/lateral)"/><option value="Palmar metacarpal vessels (medial/lateral)"/><option value="Unsure"/></datalist><i id="questionCorrect0"></i>');
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
        expect(displayCheck("Unsure", "something", true)).toBe('<font color="#663399">The right answer is something</font>');
    });
    it("toggleEnableState to correctly enable or disable class field of element", function () {
        var testElementDiv = document.createElement("input");
        var testElement = testElementDiv.id = "testing";
        expect(disableClass(testElement, true)).toBe((testElement.disabled = true));
        expect(disableClass(testElement, true)).toBe((testElement.disabled = false));
    });
});
