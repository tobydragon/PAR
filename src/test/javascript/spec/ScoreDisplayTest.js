describe("ScoreDisplay", function () {
    it("generateScoreStringByType", function () {
        var scoreJsonObj = {
            "plane": "XOOX",
            "struct": "O~~_",
            "zone": "_X__"
        };
        let element = generateScoreElementByType(scoreJsonObj);
        console.log(element);
        expect(element.getAttribute("class")).toContain('textAlignRight');
        expect(element.childNodes.item(0).getAttribute("class")).toContain('black');
        expect(element.childNodes.item(0).textContent).toBe('plane: ');
        expect(element.childNodes.item(1).textContent).toBe('struct: ');
        expect(element.childNodes.item(2).textContent).toBe('zone: ');
        expect(element.childNodes.item(2).textContent).not.toBe('plane: ');


        expect(element.childNodes.item(0).childNodes.item(1).getAttribute("class")).toContain('fas fa-times-circle red');
        expect(element.childNodes.item(0).childNodes.item(2).getAttribute("class")).toContain('fas fa-check-circle green');
        expect(element.childNodes.item(0).childNodes.item(3).getAttribute("class")).not.toContain('fas fa-minus-circle yellow');

        expect(element.childNodes.item(1).childNodes.item(1).getAttribute("class")).not.toContain('fas fa-minus-circle yellow');
        expect(element.childNodes.item(1).childNodes.item(2).getAttribute("class")).toContain('fas fa-minus-circle yellow');

        expect(element.childNodes.item(2).childNodes.item(1).getAttribute("class")).toContain('fas fa-circle');
        expect(element.childNodes.item(2).childNodes.item(2).getAttribute("class")).toContain('fas fa-times-circle red');
        expect(element.childNodes.item(2).childNodes.item(3).getAttribute("class")).toContain('fas fa-circle light-grey');
        expect(element.childNodes.item(2).childNodes.item(4).getAttribute("class")).toContain('fas fa-circle light-grey');

    });
    it("generateScoreByType", function () {
        var scoreJsonObj = {
            "plane": 85,
            "struct": 58,
            "zone": 20,
            "xyz": -1
        };
        let element = generateScoreByType(scoreJsonObj);
        console.log(element);
        expect(element.getAttribute("class")).toContain('textAlignRight');

        expect(element.childNodes.item(0).getAttribute('class')).toContain('black');

        expect(element.childNodes.item(0).textContent).toBe('plane: 85');
        expect(element.childNodes.item(1).textContent).toBe('struct: 58');
        expect(element.childNodes.item(2).textContent).toBe('zone: 20');
        expect(element.childNodes.item(3).textContent).toBe('xyz: ');
        expect(element.childNodes.item(2).textContent).not.toBe('plane: 85');

        console.log(element.childNodes.item(0).childNodes.item(1));
        expect(element.childNodes.item(0).childNodes.item(1).getAttribute("class")).toContain('green');

        expect(element.childNodes.item(1).childNodes.item(1).getAttribute("class")).toContain('orange');

        expect(element.childNodes.item(2).childNodes.item(1).getAttribute("class")).toContain('red');

        expect(element.childNodes.item(3).childNodes.item(1).getAttribute("class")).not.toContain('red');


    });
    it("buildElement", function () {
        let value = 'X';
        //expect(buildVisualSegment(value)).toBe('<div class="fas fa-times-circle red">');
        expect(buildVisualSegment(value)).toHaveClass('fas');
        expect(buildVisualSegment(value)).toHaveClass('fa-times-circle');
        expect(buildVisualSegment(value)).toHaveClass('red');
    });
});
