describe("ReadImageTaskTest", function () {
    it("checks to make sure the formatting of the plane quation is correct", function () {
        expect(setPlane()).toBe('<p>What plane is this?</p> <input type="radio" name="plane" value="Lateral"> Lateral<br> <input type="radio" name="plane" value="Transverse"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>');
    });
    it("checks to make sure a radio button zone question is created", function () {
        var test = {
            id: "ZoneQ1",
            difficulty: 4,
            questionText: "In what zone is this ultrasound taken?",
            correctAnswer: "3C",
            possibleAnswers: ["1A", "1B", "2A", "2B", "2C", "3A", "3B", "3C"],
            imageUrl: "../static/images/demoEquine02.jpg"
        };
        expect(createZoneQuestion(test)).toBe('<p> What zone is this? </p><br> <input type="radio" name="zone" value="1A">1A<br><br> <input type="radio" name="zone" value="1B">1B<br><br> <input type="radio" name="zone" value="2A">2A<br><br> <input type="radio" name="zone" value="2B">2B<br><br> <input type="radio" name="zone" value="2C">2C<br><br> <input type="radio" name="zone" value="3A">3A<br><br> <input type="radio" name="zone" value="3B">3B<br><br> <input type="radio" name="zone" value="3C">3C<br>');
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
        expect(createFillIn(test, "structure0")).toBe('<p>What structure is in the near field?</p> <input name="structure0" list="structure0"/> <datalist id="structure0"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/></datalist>');
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
        expect(createFillIn(test, "attachment1")).toBe('<p>Based on this structure, what is the distal attachment?</p> <input name="attachment1" list="attachment1"/> <datalist id="attachment1"><option value="bone"/><option value="ligament"/><option value="tumor"/><option value="tendon"/><option value="Unsure"/></datalist>');
    });
    it("check displayImageURL to see if it outputs the right image", function () {
        var test = {
            imageURL: "../static/images/demoEquine02.jpg"
        };
        expect(displayImageURL(test.imageURL)).toBe('<img class="imgCenter" src="../static/images/demoEquine02.jpg">');
    });
});
