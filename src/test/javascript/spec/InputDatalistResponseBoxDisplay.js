'use strict';

describe("InputDatalistResponseBoxDisplay", function () {
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
        expect(element.options.item(element.options.length - 1).value).toBe("unsure");
    });

    it("checkAnyResponse", function () {
        let textEntryResponseBox = new InputDatalistResponseBoxDisplay("test1", ["high", "middle", "low"], "low");
        document.getElementById("testArea").appendChild(textEntryResponseBox.element);
        document.getElementById("testArea").style.display = "none";

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
        expect(checkAnyResponse("ab", "a b")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("aB", "Ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "b  ")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab  ")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("ab", "ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("aB", "Ba")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "b  ")).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab  ")).toBe(ResponseResult.incorrect);

        expect(checkAnyResponse("anything", ResponseResult.unsure)).toBe(ResponseResult.unsure);
        expect(checkAnyResponse("anything", " " + ResponseResult.unsure.toUpperCase() + " ")).toBe(ResponseResult.unsure);
        //unsure shouldn't be authored as the correct answer, but if it is, it should return correct when entered
        expect(checkAnyResponse(ResponseResult.unsure, ResponseResult.unsure)).toBe(ResponseResult.correct);

        expect(checkAnyResponse("anything", ResponseResult.blank)).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", " ")).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", "  ")).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", " \t")).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", " \n")).toBe(ResponseResult.blank);
        //blank shouldn't be authored as the correct answer, but if it is, it should return correct when entered
        expect(checkAnyResponse(ResponseResult.blank, ResponseResult.blank)).toBe(ResponseResult.correct);
    });

    it("checkThisResponse", function () {
        let textEntryResponseBox = new InputDatalistResponseBoxDisplay("test1", ["high", "middle", "low"], "low");
        document.getElementById("testArea").appendChild(textEntryResponseBox.element);
        document.getElementById("testArea").style.display = "none";
        textEntryResponseBox.inputTextbox.value = "low";
        let test= new Response("test1");

        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.correct);
        textEntryResponseBox.inputTextbox.value = "low ";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.correct);
        textEntryResponseBox.inputTextbox.value = "LOW";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.correct);
        textEntryResponseBox.inputTextbox.value = "HIGH";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.incorrect);
        textEntryResponseBox.inputTextbox.value = "high";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.incorrect);
        textEntryResponseBox.inputTextbox.value = "something";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.incorrect);
    });
});
