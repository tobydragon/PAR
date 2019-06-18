describe("AnswerCheck", function () {
    it("displayCheck to return correct feedback given a value", function () {
        expect(displayCheck("Correct")).toBe("<font color=\"green\">Your answer is: Correct</font>");
        expect(displayCheck("Incorrect")).toBe("<font color=\"red\">Your answer is: Incorrect</font>");
        expect(displayCheck("Unsure")).toBe("<font color=\"#663399\">Your answer is: Unsure</font>");
    });

    it("toggleShowState to hide or show element correctly", function () {
        expect(toggleShowState("testElementShow")).toContain(document.getElementById("testElementShow").classlist.hide);
        expect(toggleShowState("testElementHide")).toContain(document.getElementById("testElementShow").classlist.show);
    });

});
