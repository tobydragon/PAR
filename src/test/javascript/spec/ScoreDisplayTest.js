describe("ScoreDisplay", function () {
    it("generateScoreStringByType", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "X~~_",
            "zone": "____"
        };
        expect(generateScoreStringByType(scoreJsonObj)).toBe('&nbsp <i class=black>plane: </i><i class="fas fa-times-circle red"></i><i class="fas fa-check-circle green"></i><i class="fas fa-check-circle green"></i><i class="fas fa-times-circle red"></i><br />&nbsp <i class=black>struct: </i><i class="fas fa-times-circle red"></i><i class="fas fa-minus-circle yellow"></i><i class="fas fa-minus-circle yellow"></i><i class="fas fa-circle"></i><br />&nbsp <i class=black>zone: </i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><br />');
    });
    it("generateScoreByType", function () {
        var scoreJsonObj = {
            "plane": 70,
            "struct": 40,
            "zone": 20
        };
        expect(generateScoreByType(scoreJsonObj)).toBe('<i class=black>plane:</i> <i class=orange>70<i><br /><i class=black>struct:</i> <i class=red>40<i><br /><i class=black>zone:</i> <i class=red>20<i><br />');
    });
    it("setCurrentScore", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "X~~_",
            "zone": "____"
        };
        expect(setCurrentScore(scoreJsonObj)).toBe('&nbsp <i class=black>plane: </i><i class="fas fa-times-circle red"></i><i class="fas fa-check-circle green"></i><i class="fas fa-check-circle green"></i><i class="fas fa-times-circle red"></i><br />&nbsp <i class=black>struct: </i><i class="fas fa-times-circle red"></i><i class="fas fa-minus-circle yellow"></i><i class="fas fa-minus-circle yellow"></i><i class="fas fa-circle"></i><br />&nbsp <i class=black>zone: </i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><br />')
    });
});
