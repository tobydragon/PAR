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

    it("checkResponse", function () {
        expect(checkResponse("a", "a")).toBe(ResponseResult.correct);
        expect(checkResponse(" a ", "a")).toBe(ResponseResult.correct);
        expect(checkResponse(" A ", "a")).toBe(ResponseResult.correct);
        expect(checkResponse("a", " a ")).toBe(ResponseResult.correct);
        expect(checkResponse("a", " A ")).toBe(ResponseResult.correct);
        expect(checkResponse("aA ", " Aa")).toBe(ResponseResult.correct);
        expect(checkResponse("aB", " Ab ")).toBe(ResponseResult.correct);

        expect(checkResponse("a", "b")).toBe(ResponseResult.incorrect);
        expect(checkResponse("a", "ab")).toBe(ResponseResult.incorrect);
        expect(checkResponse("ab", "ba")).toBe(ResponseResult.incorrect);
        expect(checkResponse("aB", "Ba")).toBe(ResponseResult.incorrect);
        expect(checkResponse("a", "b  ")).toBe(ResponseResult.incorrect);
        expect(checkResponse("a", "ab  ")).toBe(ResponseResult.incorrect);
    });
});