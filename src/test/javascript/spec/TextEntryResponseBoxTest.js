'use strict';

describe("TextEntryResponseBox", function () {
    it("buildOptionElement", function () {
        let element = buildOptionElement("nope");
        expect(element.tagName.toLowerCase()).toBe("option");
        expect(element.getAttribute("value")).toBe("nope");
    });

    it("buildDatalistElement", function () {
        let element = buildDatalistElement("test1", ["1", "2", "3"]);
        expect(element.tagName.toLowerCase()).toBe("datalist");
        expect(element.id.toLowerCase()).toBe(("test1Datalist").toLowerCase());
        expect(element.options.length).toBe(4);
        expect(element.options.item(0).value).toBe("1");
        expect(element.options.item(element.options.length-1).value).toBe("unsure");
    });

    it("checkAnyResponse", function () {
        expect(checkAnyResponse("a", "a")).toBe(ResponseResult.correct);
        expect(checkAnyResponse(" a ", "a")).toBe(ResponseResult.correct);
        expect(checkAnyResponse(" A ", "a")).toBe(ResponseResult.correct);
        expect(checkAnyResponse("a", " a ")).toBe(ResponseResult.correct);
        expect(checkAnyResponse("a", " A ")).toBe(ResponseResult.correct);
        expect(checkAnyResponse("aA ", " Aa")).toBe(ResponseResult.correct);
        expect(checkAnyResponse("aB", " Ab ")).toBe(ResponseResult.correct);

        expect(checkAnyResponse("a", "b")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("ab", "ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("aB", "Ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "b  ")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab  ")).toBe(ResponseResult.incorrect);

        expect(checkAnyResponse("ab", "ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("aB", "Ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "b  ")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab  ")).toBe(ResponseResult.incorrect);

        expect(checkAnyResponse("anything", ResponseResult.unsure)).toBe(ResponseResult.unsure);
        expect(checkAnyResponse("anything", " "+ ResponseResult.unsure.toUpperCase()+" ")).toBe(ResponseResult.unsure);

    });

    it("checkThisResponse", function () {
        let textEntryResponseBox = new TextEntryResponseBox("test1", ["high", "middle", "low"], "low");
        textEntryResponseBox.inputTextbox.value = "low";
        expect(textEntryResponseBox.checkCurrentResponse()).toBe(ResponseResult.correct);
        textEntryResponseBox.inputTextbox.value = "low ";
        expect(textEntryResponseBox.checkCurrentResponse()).toBe(ResponseResult.correct);
        textEntryResponseBox.inputTextbox.value = "LOW";
        expect(textEntryResponseBox.checkCurrentResponse()).toBe(ResponseResult.correct);
        textEntryResponseBox.inputTextbox.value = "HIGH";
        expect(textEntryResponseBox.checkCurrentResponse()).toBe(ResponseResult.incorrect);
        textEntryResponseBox.inputTextbox.value = "high";
        expect(textEntryResponseBox.checkCurrentResponse()).toBe(ResponseResult.incorrect);
        textEntryResponseBox.inputTextbox.value = "something";
        expect(textEntryResponseBox.checkCurrentResponse()).toBe(ResponseResult.incorrect);
    });
});