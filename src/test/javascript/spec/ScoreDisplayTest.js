describe("ScoreDisplay", function () {
    it("generateScoreStringByType", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "O~~_",
            "zone": "_X__"
        };
        expect(generateScoreElementByType(scoreJsonObj)).toEqual('<div><div class="black">plane: <div class="fas fa-times-circle red" aria-hidden="true"></div><div class="fas fa-check-circle green" aria-hidden="true"></div><div class="fas fa-check-circle green" aria-hidden="true"></div><div class="fas fa-times-circle red" aria-hidden="true"></div></div><div class="black">struct: <div class="fas fa-check-circle green" aria-hidden="true"></div><div class="fas fa-minus-circle yellow" aria-hidden="true"></div><div class="fas fa-minus-circle yellow" aria-hidden="true"></div><div class="fas fa-circle" aria-hidden="true"></div></div><div class="black">zone: <div class="fas fa-circle" aria-hidden="true"></div><div class="fas fa-times-circle red" aria-hidden="true"></div><div class="fas fa-circle" aria-hidden="true"></div><div class="fas fa-circle" aria-hidden="true"></div></div></div>');
        console.log(scoreJsonObj);
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('black');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('fas');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('fa-times-circle');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('green');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('red');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('yellow');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('fa-circle');
        expect(generateScoreElementByType(scoreJsonObj)).toHaveClass('fa-check-circle');
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
        //expect(setCurrentScore(scoreJsonObj)).toBe('<div><div class="black">plane: <div class="fas fa-times-circle red" aria-hidden="true"></div><div class="fas fa-check-circle green" aria-hidden="true"></div><div class="fas fa-check-circle green" aria-hidden="true"></div><div class="fas fa-times-circle red" aria-hidden="true"></div></div><div class="black">struct: <div class="fas fa-check-circle green" aria-hidden="true"></div><div class="fas fa-minus-circle yellow" aria-hidden="true"></div><div class="fas fa-minus-circle yellow" aria-hidden="true"></div><div class="fas fa-circle" aria-hidden="true"></div></div><div class="black">zone: <div class="fas fa-circle" aria-hidden="true"></div><div class="fas fa-times-circle red" aria-hidden="true"></div><div class="fas fa-circle" aria-hidden="true"></div><div class="fas fa-circle" aria-hidden="true"></div></div></div>');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('black');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('fas');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('fa-times-circle');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('green');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('red');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('yellow');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('fa-circle');
        expect(setCurrentScore(scoreJsonObj, "ByType")).toHaveClass('fa-check-circle');

    });
    it("buildELement", function () {
        let value = 'X';
        //expect(buildVisualSegment(value)).toBe('<div class="fas fa-times-circle red">');
        expect(buildVisualSegment(value)).toHaveClass('fas');
        expect(buildVisualSegment(value)).toHaveClass('fa-times-circle');
        expect(buildVisualSegment(value)).toHaveClass('red');
    });
});
