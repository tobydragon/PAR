describe("AnswerCheckControl", function () {
    it("check that the JSON response that is being generated is correct", function () {
        var testResponse= {
            userId: "Hewwo123",
            taskQuestionIds: [ 'PlaneQ1', 'StructureQ1', 'ZoneQ1' ],
            responseTexts: [ 'Lateral', 'ligament', 'Unsure' ]
        };
        expect(testGenerateReponseJSON()).toBe(testResponse);
    });
});