'use strict';

describe("FileUtil", function () {
    // it("readJsonAsync", function () {
    //     //readJsonFile("/Users/Documents/workspace/test.json", finishReadJsonTest);
    //     //TODO: understand how to test async functions, see: https://stackoverflow.com/questions/27344872/testing-asynchronous-callbacks-with-jasmine
    // });

    // function finishReadJsonTest(resultingObject){
    //     expect(resultingObject.length).toBe(47);
    //     expect(resultingObject[0].id).toBe("plane./images/demoEquine14.jpg");
    // }

    it("readJson", function () {

        let resultingObject = readJson("../resources/author/nextImageTaskTest1.json");
        expect(resultingObject.imageUrl).toBe("./images/demoEquine04.jpg");

        resultingObject = readJson("../resources/author/DemoQuestionPool.json");
        expect(resultingObject.length).toBe(47);
        expect(resultingObject[0].id).toBe("plane./images/demoEquine14.jpg");
    });
});