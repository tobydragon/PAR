describe("ScoreDisplayTest", function () {
    it("make sure ScoreDisplay creates correct visualization string", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "X~~_",
            "zone": "____"
        }
        expect(setCurrentScore(scoreJsonObj)).toBe("");
    })
});
