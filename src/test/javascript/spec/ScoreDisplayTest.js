describe("ScoreDisplay", function () {
    it("generateScoreStringByType", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "O~~_",
            "zone": "_X__"
        };
        expect(generateScoreElementByType(scoreJsonObj)).toBe( < div > < div class = "black" > plane: < div class = "fas fa-times-circle red"
            aria - hidden = "true" > < /div><div class="fas fa-check-circle green" aria-hidden="true"></div > < div class = "fas fa-check-circle green"
            aria - hidden = "true" > < /div><div class="fas fa-times-circle red" aria-hidden="true"></div > < /div><div class="black">struct: <div class="fas fa-check-circle green" aria-hidden="true"></div > < div class = "fas fa-minus-circle yellow"
            aria - hidden = "true" > < /div><div class="fas fa-minus-circle yellow" aria-hidden="true"></div > < div class = "fas fa-circle"
            aria - hidden = "true" > < /div></div > < div class = "black" > zone: < div class = "fas fa-circle"
            aria - hidden = "true" > < /div><div class="fas fa-times-circle red" aria-hidden="true"></div > < div class = "fas fa-circle"
            aria - hidden = "true" > < /div><div class="fas fa-circle" aria-hidden="true"></div > < /div></div > );
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
            "struct": "O~~_",
            "zone": "_X__"
        };
        expect(setCurrentScore(scoreJsonObj)).toBe( < div > < div class = "black" > plane: < div class = "fas fa-times-circle red"
            aria - hidden = "true" > < /div><div class="fas fa-check-circle green" aria-hidden="true"></div > < div class = "fas fa-check-circle green"
            aria - hidden = "true" > < /div><div class="fas fa-times-circle red" aria-hidden="true"></div > < /div><div class="black">struct: <div class="fas fa-check-circle green" aria-hidden="true"></div > < div class = "fas fa-minus-circle yellow"
            aria - hidden = "true" > < /div><div class="fas fa-minus-circle yellow" aria-hidden="true"></div > < div class = "fas fa-circle"
            aria - hidden = "true" > < /div></div > < div class = "black" > zone: < div class = "fas fa-circle"
            aria - hidden = "true" > < /div><div class="fas fa-times-circle red" aria-hidden="true"></div > < div class = "fas fa-circle"
            aria - hidden = "true" > < /div><div class="fas fa-circle" aria-hidden="true"></div > < /div></div > )
    });
});
