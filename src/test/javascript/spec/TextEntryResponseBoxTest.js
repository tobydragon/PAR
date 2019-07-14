'use strict';

describe("TextEntryResponseBox", function () {
    it("buildOptionElement", function () {
        let optionElement = buildOptionElement("nope");
        expect(optionElement.getAttribute("value")).toBe("nope");
    });
});