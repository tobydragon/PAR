'use strict';

describe("AnswerView", function () {
    it('getCurrentAnswer', function(){
        let qaModel = {
            id: "question31",
            possibleAnswers: ["transverse", "Both proximal and middle phalanxes"],
            correctAnswer: "transverse"
        };
        const defaultQaSettings = {
            unsureShowsCorrect: true
        };
       let ansViewObj= new AnswerView(qaModel, defaultQaSettings);
       let badAnswer="some string";
       expect(ansViewObj.getCurrentAnswer()).not.toBe(null);
       expect(ansViewObj.getCurrentAnswer()).not.toBe(badAnswer);
       expect(ansViewObj.getCurrentAnswer()).toBe(ansViewObj.inputTextbox.value);
    });
    it("checkAnswerAndUpdateView", function(){
        let qaModel = {
            id: "question48",
            possibleAnswers: ["transverse", "Suspensory ligament (body)","proximal bone"],
            correctAnswer: "proximal bone"
        };
        const defaultQaSettings = {
            unsureShowsCorrect: true
        };
        let ansViewObj= new AnswerView(qaModel, defaultQaSettings);
        ansViewObj.qaModel.correctResponse="proximal bone";
        //with incorrect value
        ansViewObj.inputTextbox.value="longitudinal";
        expect(ansViewObj.checkAnswerAndUpdateView()).toBe("incorrect");
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("correct");
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("");
        //with correct value
        ansViewObj.inputTextbox.value="proximal bone";
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("incorrect");
        expect(ansViewObj.checkAnswerAndUpdateView()).toBe("correct");
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("");
        //with blank
        ansViewObj.inputTextbox.value="";
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("incorrect");
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("correct");
        expect(ansViewObj.checkAnswerAndUpdateView()).toBe("");
        //with unsure
        ansViewObj.inputTextbox.value="unsure";
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("incorrect");
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("correct");
        expect(ansViewObj.checkAnswerAndUpdateView()).not.toBe("");
        expect(ansViewObj.checkAnswerAndUpdateView()).toBe("unsure");
    });
    it("buildOptionElement", function () {
        let element = buildOptionElement("nope");
        expect(element.tagName.toLowerCase()).toBe("option");
        expect(element.getAttribute("value")).toBe("nope");
    });

    it("buildDatalistElement", function () {
        let element = buildDatalistElement("test1", ["1", "2", "3"]);
        expect(element.tagName.toLowerCase()).toBe("datalist");
        expect(element.id.toLowerCase()).toBe(("test1Datalist").toLowerCase());
        expect(element.options.length).toBe(3);
        expect(element.options.item(0).value).toBe("1");
        expect(element.options.item(element.options.length - 1).value).toBe('3');
    });

    it("addToTypesIncorrect", function () {
        let testList = [];
        addToTypesIncorrect("correct", "plane", testList);
        expect(testList.length).toBe(0);
        addToTypesIncorrect("correct", "ZONE", testList);
        expect(testList.length).toBe(0);
        addToTypesIncorrect("correct", "", testList);
        expect(testList.length).toBe(0);

        addToTypesIncorrect("incorrect", "plane", testList);
        expect(testList.length).toBe(1);
        addToTypesIncorrect("incorrect", "ZONE", testList);
        expect(testList.length).toBe(2);
        addToTypesIncorrect("incorrect", "", testList);
        expect(testList.length).toBe(3);

        addToTypesIncorrect("incorrect", "plane", testList);
        expect(testList.length).toBe(3);
        addToTypesIncorrect("incorrect", "ZONE", testList);
        expect(testList.length).toBe(3);
        addToTypesIncorrect("incorrect", "", testList);
        expect(testList.length).toBe(3);

        addToTypesIncorrect("unsure", "plane1", testList);
        expect(testList.length).toBe(4);
        addToTypesIncorrect("unsure", "ZONE1", testList);
        expect(testList.length).toBe(5);
        addToTypesIncorrect("unsure", "1", testList);
        expect(testList.length).toBe(6);

        addToTypesIncorrect("", "plane2", testList);
        expect(testList.length).toBe(6);
        addToTypesIncorrect("", "ZONE2", testList);
        expect(testList.length).toBe(6);
        addToTypesIncorrect("", "2", testList);
        expect(testList.length).toBe(6);
    });

    it("makeFeedbackHtml", function () {
        let textEntryResponseBox = new AnswerView("test1", ["high", "middle", "low"], "low");
        document.getElementById("testArea").appendChild(textEntryResponseBox.element);
        document.getElementById("testArea").style.display = "none";

        expect(makeFeedbackHtml("correct", "Long", true)).toBe('<font color=\"green\">Your answer is: Correct</font>');
        expect(makeFeedbackHtml("correct", "Long", false)).toBe('<font color=\"green\">Your answer is: Correct</font>');

        expect(makeFeedbackHtml("unsure", "Long", true)).toBe("<font color=\"#663399\">The correct answer is Long</font>");
        expect(makeFeedbackHtml("unsure", "Long", false)).toBe("");

        expect(makeFeedbackHtml("", "Long", true)).toBe("");
        expect(makeFeedbackHtml("", "Long", false)).toBe("");

        expect(makeFeedbackHtml("incorrect", "Long", true)).toBe('<font color=\"red\">Your answer is: Incorrect</font>');
        expect(makeFeedbackHtml("incorrect", "Long", false)).toBe('<font color=\"red\">Your answer is: Incorrect</font>');
    });

    it("checkAnyResponse", function () {
        let textEntryResponseBox = new AnswerView("test1", ["high", "middle", "low"], "low");

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
        let textEntryResponseBox = new AnswerView("test1", ["high", "middle", "low"], "low");
        document.getElementById("testArea").appendChild(textEntryResponseBox.element);
        document.getElementById("testArea").style.display = "none";
        textEntryResponseBox.inputTextbox.value = "low";
        let test = new Response("test1",[],[]);
        let unsureShowsCorrect = false;

        expect(textEntryResponseBox.checkCurrentResponse(test, unsureShowsCorrect)).toBe(ResponseResult.correct, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "low ";
        expect(textEntryResponseBox.checkCurrentResponse(test, unsureShowsCorrect)).toBe(ResponseResult.correct, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "LOW";
        expect(textEntryResponseBox.checkCurrentResponse(test, unsureShowsCorrect)).toBe(ResponseResult.correct, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "HIGH";
        expect(textEntryResponseBox.checkCurrentResponse(test, unsureShowsCorrect)).toBe(ResponseResult.incorrect, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "high";
        expect(textEntryResponseBox.checkCurrentResponse(test, unsureShowsCorrect)).toBe(ResponseResult.incorrect, unsureShowsCorrect);
        textEntryResponseBox.inputTextbox.value = "something";
        expect(textEntryResponseBox.checkCurrentResponse(test, unsureShowsCorrect)).toBe(ResponseResult.incorrect, unsureShowsCorrect);
    });
    it("disableElement", function () {
        let testElement = document.createElement('button');
        testElement.textContent = "test button";
        expect(testElement.disabled).toBe(false);
        expect(disableElement(testElement)).toBe(true);
    });
    it("calcTextSizeFromPossStrings", function () {
        let listOfStr0 = ["one", "three", "four", "asuperlongwordthatgoespasttwentyttt"];
        let listOfStr1 = ["one", "the", "for"];
        let listOfStr2 = ["one", "three", " "];
        let listOfStr3 = ["o", "tw", "thr"];
        let listOfStr4 = ["o"];
        let listOfStr5 = ["lllllllloooooooonnng boi tada", "transverse", "zagga"];
        let listOfStr6 = ["lllllllloooooooonnng boi tada", "transverse", "Both the zip (and the -zorp) act in such a way a _WIDE_ boy cannot possibly; well; understand!"];
        expect(calcTextSizeFromPossStrings(listOfStr0)).toBe(30);
        expect(calcTextSizeFromPossStrings(listOfStr1)).toBe(20);
        expect(calcTextSizeFromPossStrings(listOfStr2)).toBe(20);
        expect(calcTextSizeFromPossStrings(listOfStr3)).toBe(20);
        expect(calcTextSizeFromPossStrings(listOfStr4)).toBe(20);
        expect(calcTextSizeFromPossStrings(listOfStr5)).toBe(25);
        expect(calcTextSizeFromPossStrings(listOfStr6)).toBe(79);
    })
});
