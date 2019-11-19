
class ImageTaskView{

    constructor(imageTaskModel, userId){
        this.id = userId + "-" + imageTaskModel.imageUrl;
        this.imageView = new ImageView(imageTaskModel.imageUrl, this.id+"Canvas");
        this.questionTreeList = buildQATreeViewList(imageTaskModel.taskQuestions);
        this.element = buildImageTaskViewElement(this.id, this.imageView, this.questionTreeList);
    }
}

function buildImageTaskViewElement(id, imageView, questionTreeList){
    let outerImageTaskNode = document.createElement('div');
    outerImageTaskNode.classList.add('row');

    imageView.loadImage();
    let canvasElementHandler = document.createElement('div');
    canvasElementHandler.classList.add('col-6');
    canvasElementHandler.classList.add('imgCenter');
    canvasElementHandler.appendChild(imageView.element);

    let questionAreaNode = createQuestionAreaElement(questionTreeList);
    let questionAreaElementHandler = document.createElement('div');
    questionAreaElementHandler.classList.add('col-4');
    let questionTitleElement = document.createElement('h1');
    questionTitleElement.classList.add('text-center');
    questionTitleElement.textContent = "Question Set";

    let feedbackElement = document.createElement('i');
    feedbackElement.setAttribute('id', 'helpfulFeedback' + this.counter);
    feedbackElement.classList.add('text-center');

    questionAreaElementHandler.appendChild(questionTitleElement);
    questionAreaElementHandler.appendChild(questionAreaNode);
    questionAreaElementHandler.appendChild(feedbackElement);
    questionAreaElementHandler.appendChild(createSubmitButtonElement(questionTreeList));

    let spaceNode0 = document.createElement('div');
    spaceNode0.classList.add('col-1');
    let spaceNode1 = document.createElement('div');
    spaceNode1.classList.add('col-1');

    outerImageTaskNode.appendChild(spaceNode0);
    outerImageTaskNode.appendChild(canvasElementHandler);
    outerImageTaskNode.appendChild(questionAreaElementHandler);
    outerImageTaskNode.appendChild(spaceNode1);

    outerImageTaskNode.appendChild(spaceNode0);
    outerImageTaskNode.appendChild(canvasElementHandler);
    outerImageTaskNode.appendChild(questionAreaElementHandler);
    outerImageTaskNode.appendChild(spaceNode1);

    return outerImageTaskNode;
}

function createQuestionAreaElement(questionTreeList) {
    let outerQuestionSetNode = document.createElement('form');
    outerQuestionSetNode.setAttribute('action', '#');
    outerQuestionSetNode.setAttribute('method', 'post');
    outerQuestionSetNode.setAttribute('id', 'questionSetForm');

    let questionElement = document.createElement('div');
    questionElement.setAttribute('id', 'questionSet');

    for (let questionTreeView of questionTreeList){
        questionElement.appendChild(questionTreeView.element);
    }
    outerQuestionSetNode.appendChild(questionElement);
    return outerQuestionSetNode;
}

function createSubmitButtonElement(questionTreeList) {
    //TODO: what is "this" in this scenario? Is this functional? It is needed for the button to work currently...
    this.questionTreeList = questionTreeList;
    let submitButtonElement = document.createElement('button');
    submitButtonElement.setAttribute('type', 'button');
    submitButtonElement.classList.add('btn');
    submitButtonElement.classList.add('btn-primary');
    submitButtonElement.setAttribute('id', 'submitButton' + this.counter);

    submitButtonElement.setAttribute('onclick', 'console.log(getResponsesFromAllQATreesInFlatList(questionTreeList))');
    submitButtonElement.textContent = 'Submit';

    return submitButtonElement;
}