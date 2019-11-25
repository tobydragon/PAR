
class ImageTaskView{

    constructor(imageTaskModel, userId){
        this.userId = userId;
        this.id = userId + "-" + imageTaskModel.imageUrl;
        this.imageView = new ImageView(imageTaskModel.imageUrl, this.id+"Canvas");
        this.questionTreeList = buildQATreeViewList(imageTaskModel.taskQuestions);
        this.element = buildImageTaskViewElement(this.id, this.imageView, this.questionTreeList);
    }

    checkAnswersAndUpdateView(){
        for (let qaTree of this.questionTreeList){
            qaTree.checkAnswersAndUpdateView();
        }
    }

    showAllLevelFollowupQuestions(){
        for (let qaTree of this.questionTreeList){
            qaTree.showAllLevelFollowupQuestions();
        }
    }

    getResponse(){
        return new ImageTaskResponse(this.userId, getResponsesFromAllQATreesInFlatList(this.questionTreeList));
    }
}

function buildImageTaskViewElement(id, imageView, questionTreeList){
    let outerImageTaskNode = document.createElement('div');
    outerImageTaskNode.classList.add('row');

    let canvasElementHandler = document.createElement('div');
    canvasElementHandler.classList.add('col-6');
    canvasElementHandler.classList.add('imgCenter');
    imageView.loadImage();
    canvasElementHandler.appendChild(imageView.element);

    let questionAreaNode = createQuestionAreaElement(questionTreeList);
    let questionAreaElementHandler = document.createElement('div');
    questionAreaElementHandler.classList.add('col-4');
    let questionTitleElement = document.createElement('h1');
    questionTitleElement.classList.add('text-center');
    questionTitleElement.textContent = "Question Set";
    questionAreaElementHandler.appendChild(questionTitleElement);
    questionAreaElementHandler.appendChild(questionAreaNode);

    let spaceNode0 = document.createElement('div');
    spaceNode0.classList.add('col-1');
    let spaceNode1 = document.createElement('div');
    spaceNode1.classList.add('col-1');

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
