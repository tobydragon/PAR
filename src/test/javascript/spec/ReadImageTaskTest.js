describe("ReadImageTaskTest", function () {
    it("checks to make sure the formatting of the plane quation is correct", function () {
        expect(setPlane()).toBe('<p>What plane is this?</p> <input type="radio" name="plane" value="Lateral"> Lateral<br> <input type="radio" name="plane" value="Transverse"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>');
    });
    it("checks to make sure a radio button zone question is created", function (){
        var test= {
            id : "ZoneQ1",
            difficulty : 4,
            questionText : "In what zone is this ultrasound taken?",
            correctAnswer : "3C",
            possibleAnswers : [ "1A", "1B", "2A", "2B", "2C", "3A", "3B", "3C" ],
            imageUrl : "../static/images/demoEquine02.jpg"
        };
        expect(createZoneQuestion(test)).toBe('<p>What zone is this?</p> <input type="radio" name="zone" value="Incorrect"> 1A<br> <input type="radio" name="zone" value="Correct"> 1B<br> <input type="radio" name="zone" value="Incorrect"> 2A<br> <input type="radio" name="zone" value="Incorrect"> 2B<br> <input type="radio" name="zone" value="Incorrect"> 2C<br> <input type="radio" name="zone" value="Incorrect"> 3A<br> <input type="radio" name="zone" value="Incorrect"> 3B<br> <input type="radio" name="zone" value="Incorrect"> 3C<br>');
    });
    it("check to make sure createFillin makes a good structure question", function(){
        var test= {
            id : "structureQ1",
            difficulty : 2,
            questionText : "What structure is in the near field?",
            correctAnswer : "bone",
            possibleAnswers : [ "bone", "ligament", "tumor", "tendon", "I don't know" ],
            imageUrl : "../static/images/demoEquine02.jpg"
        };
        expect(createFillIn(test, "structure0")).tobe();
    });
    it("check to make sure createFillin makes a good attachment question", function(){
        var test= {
            id : "attachmentQ1",
            difficulty : 3,
            questionText : "What is this structure's distal attachment?",
            correctAnswer : "tendon",
            possibleAnswers : [ "bone", "ligament", "tumor", "tendon", "I don't know" ],
            imageUrl : "../static/images/demoEquine02.jpg"
        };
        expect(createFillIn(test, "attachment1")).tobe();
    });

});