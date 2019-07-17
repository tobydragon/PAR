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
        let type= "long";
        let typesIncorrect=[];
        let textArea=document.getElementById("testArea");
        let unsureShowsCorrect= false;

        expect(checkAnyResponse("a", "a", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
        expect(checkAnyResponse(" a ", "a", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
        expect(checkAnyResponse(" A ", "a", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
        expect(checkAnyResponse("a", " a ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
        expect(checkAnyResponse("a", " A ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
        expect(checkAnyResponse("aA ", " Aa", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
        expect(checkAnyResponse("aB", " Ab ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);

        expect(checkAnyResponse("a", "b", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("ab", "ba", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("ab", "a b", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("aB", "Ba", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "b  ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab  ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("ab", "ba", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("aB", "Ba", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "b  ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);
        expect(checkAnyResponse("a", "ab  ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.incorrect);

        expect(checkAnyResponse("anything", ResponseResult.unsure, type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.unsure);
        expect(checkAnyResponse("anything", " " + ResponseResult.unsure.toUpperCase() + " ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.unsure);
        //unsure shouldn't be authored as the correct answer, but if it is, it should return correct when entered
        expect(checkAnyResponse(ResponseResult.unsure, ResponseResult.unsure, type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);

        expect(checkAnyResponse("anything", ResponseResult.blank, type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", " ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", "  ", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", " \t", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.blank);
        expect(checkAnyResponse("anything", " \n", type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.blank);
        //blank shouldn't be authored as the correct answer, but if it is, it should return correct when entered
        expect(checkAnyResponse(ResponseResult.blank, ResponseResult.blank, type, typesIncorrect, textArea, unsureShowsCorrect)).toBe(ResponseResult.correct);
    });

    it("checkThisResponse", function () {
        let textEntryResponseBox = new InputDatalistResponseBoxDisplay("test1", ["high", "middle", "low"], "low");
        document.getElementById("testArea").appendChild(textEntryResponseBox.element);
        document.getElementById("testArea").style.display = "none";
        textEntryResponseBox.inputTextbox.value = "low";
        let test = new Response("test1");
        let unsureShowsCorrect= false;

        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.correct, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "low ";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.correct, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "LOW";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.correct, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "HIGH";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.incorrect, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "high";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.incorrect, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "something";
        expect(textEntryResponseBox.checkCurrentResponse(test)).toBe(ResponseResult.incorrect, unsureShowsCorrect);
    });
    it("disableElement", function () {
        let testElement = document.createElement('button');
        testElement.textContent = "test button";
        expect(testElement.disabled).toBe(false);
        expect(disableElement(testElement)).toBe(true);
    })
});
