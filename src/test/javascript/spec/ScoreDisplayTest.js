describe("ScoreDisplay", function () {
    it("make sure ScoreDisplay creates correct visualization string", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "X~~_",
            "zone": "____"
        };
        expect(generateScoreStringByType(scoreJsonObj)).toBe('&nbsp <i class=black>plane: </i><i class="fas fa-times-circle red"></i><i class="fas fa-check-circle green"></i><i class="fas fa-check-circle green"></i><i class="fas fa-times-circle red"></i><br />&nbsp <i class=black>struct: </i><i class="fas fa-times-circle red"></i><i class="fas fa-minus-circle yellow"></i><i class="fas fa-minus-circle yellow"></i><i class="fas fa-circle"></i><br />&nbsp <i class=black>zone: </i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><i class="fas fa-circle"></i><br />');
    })
});
