describe("AnswerCheckControl", function () {
    it("generateFeedback to see if true, gives correct response", function () {
        expect(generateFeedback(true)).toBe("Correct!");
    });
    it("generateFeedback to see if gives false response correctly when sent in a false", function () {
        expect(generateFeedback(false)).toBe("Incorrect. So sad :(");
    });
    it("generateFeedbackPerQuestion to see if gives correct feedback per correct/incorrect response pairings", function () {
        expect(generateFeedbackPerQuestion(true, true, true)).toBe("All three correct!");
        expect(generateFeedbackPerQuestion(false, false, false)).toBe("All three are wrong.");
        expect(generateFeedbackPerQuestion(true, true, false)).toBe("Question 3 is wrong");
        expect(generateFeedbackPerQuestion(true, false, true)).toBe("Question 2 is wrong");
        expect(generateFeedbackPerQuestion(false, true, true)).toBe("Question 1 is wrong");
        expect(generateFeedbackPerQuestion(true, false, false)).toBe("Questions 2 and 3 are wrong");
        expect(generateFeedbackPerQuestion(false, true, false)).toBe("Questions 1 and 3 are wrong");
        expect(generateFeedbackPerQuestion(false, false, true)).toBe("Questions 1 and 2 are wrong");
    });
});
