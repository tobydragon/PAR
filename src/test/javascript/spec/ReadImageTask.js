describe("ReadImageTaskTest", function () {
    it("checks to make sure a radio button zone question is created", function () {
        var test = {
            id: "ZoneQ1",
            difficulty: 4,
            questionText: "In what zone is this ultrasound taken?",
            correctAnswer: "3C",
            possibleAnswers: ["1A", "1B", "2A", "2B", "2C", "3A", "3B", "3C"],
            imageUrl: "../static/images/demoEquine02.jpg"
        };
        var test2 = {
            id: "PlaneQ1",
            difficulty: 1,
            questionText: "What plane is this?",
            possibleAnswers: ["Transverse", "Longitudinal"],
            imageURL: "../static/images/demoEquine02.jpg"
        }
        expect(createRadioQuestion(test)).toBe('<p>In what zone is this ultrasound taken?</p><br> <input type="radio" name="q0" value="1A">1A<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="1B">1B<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="2A">2A<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="2B">2B<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="2C">2C<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="3A">3A<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="3B">3B<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="3C">3C<br> <i id="questionCorrect0"></i>');
        expect(createRadioQuestion(test2)).toBe('<p>What plane is this?</p><br> <input type="radio" name="q0" value="Transverse">Transverse<br> <i id="questionCorrect0"></i><br> <input type="radio" name="q0" value="Longitudinal">Longitudinal<br> <i id="questionCorrect0"></i>');
    });

    it("check to make sure createFillin makes a good structure question", function () {
        var test = {
            id: "structureQ1",
            difficulty: 2,
            questionText: "What structure is in the near field?",
            correctAnswer: "bone",
            possibleAnswers: ["bone", "ligament", "tumor", "tendon", "Unsure"],
            imageUrl: "../static/images/demoEquine02.jpg"
        };
        expect(createFillIn(test)).toBe('<p>What structure is in the near field?</p> <input name="q0" list="list0"/> <datalist id="list0"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/></datalist><i id="questionCorrect0"></i>');
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
        expect(createFillIn(test)).toBe('<p>Based on this structure, what is the distal attachment?</p> <input name="q0" list="list0"/> <datalist id="list0"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/></datalist><i id="questionCorrect0"></i>');
    });
    it("check displayURL that it displays the right image", function () {
        var test = {
            imageURL: "../static/images/demoEquine02.jpg"
        };
        expect(displayImageURL(generateImageURL(test.imageURL))).toBe('<img th:src="@{images/demoEquine02.jpg}" class="imgCenter"/>');
    });
    it("check generateImageURL generates the right source tag and thymeleaf source tag", function () {
        var test = {
            imageURL: "../static/images/demoEquine02.jpg"
        };
        expect(generateImageURL(test.imageURL)).toBe('<img th:src="@{images/demoEquine02.jpg}" class="imgCenter"/>');
    });
    it("check generateQuestions to see if it makes correct function calls and the counter is correct", function () {
        var test2 = {
            id: "PlaneQ1",
            difficulty: 1,
            questionText: "What plane is this?",
            possibleAnswers: ["Transverse", "Longitudinal"],
            imageURL: "../static/images/demoEquine02.jpg"
        };
        expect(getNumberOfQuestions()).toBe(0);
    });
});
