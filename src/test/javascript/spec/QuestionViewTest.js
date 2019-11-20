describe("QuestionView",function(){
   it("getResponse", function(){
    let qvObjText= new QuestionView("hello");
    let qvObjNoText= new QuestionView();
    expect(qvObjText.getResponse()).toBeNull();
    expect(qvObjNoText.getResponse()).not.toBeNull();

   });
   it("createInputQuestionElement", function(){
    let testElement=createInputQuestionElement();
    expect(testElement).not.toBeNull();
    expect(testElement.getAttribute("type")).toBe("text");
    expect(testElement.getAttribute("size")).toBe('50');
    expect(testElement.getAttribute("id")).toBe("inputQuestionArea");
   });
   it("createFixedQuestionElement", function(){
    let questionText=null;
    let fixedElement=createFixedQuestionElement(questionText);
    expect(fixedElement).not.toBeNull();
    expect(fixedElement.getAttribute("textContent")).toBeNull();
    expect(fixedElement.getAttribute("id")).toBe("fixedQuestionArea");
   });
});