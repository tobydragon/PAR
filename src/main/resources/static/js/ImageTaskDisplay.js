class ImageTaskDisplay {

    constructor(imageTaskJson, userId, imageTaskSettings, isAuthor, canvasName, pageDisplaySettings) {
        this.userId = userId;
        this.response = new Response(userId);
        this.hasFolloup = false;
        this.imageUrl = imageTaskJson.imageUrl;
        this.imageTaskJson = imageTaskJson;
        this.taskQuestions = imageTaskJson.taskQuestions;
        //settings
        this.unsureShowsCorrectAnswer = imageTaskSettings.unsureShowsCorrectAnswer;
        this.feedbackByType = imageTaskSettings.feedbackByType;
        this.willDisplayFeedback = imageTaskSettings.willDisplayFeedback;
        this.ableToResubmitAnswers = imageTaskSettings.ableToResubmitAnswers;
        this.mustSubmitAnswersToContinue = imageTaskSettings.mustSubmitAnswersToContinue;
        this.haveSubmited = 0;
        this.canGiveNoAnswer = imageTaskSettings.canGiveNoAnswer;
        this.pageSettings = pageDisplaySettings;
        this.canvasName = canvasName;
        this.listOfCorrectAnswers = [];
        this.isAuthor = isAuthor;

        //buildQuestionAreasAuthor(this.isAuthor)
        if (!isAuthor) {
            addUnsureToAnswers(imageTaskJson.taskQuestions);
        }
    }

    displayImageUrl() {
        document.getElementById("Ids").innerText = this.imageUrl;
    }

    submitAnswers() {
        this.response.responseTexts = [];
        let canContinu;
        document.getElementById("errorFeedback").innerHTML = " ";

        if (!this.isAuthor) {
            this.checkAnswers();
            canContinu = checkIfCanContinu(this.canGiveNoAnswer, this.listOfCorrectAnswers);
        } else {
            this.authorSubmitResponses();
            canContinu = true;
        }

        if (canContinu) {
            this.displayFeedback();
            sendResponse(this.response, this.ableToResubmitAnswers, this.isAuthor, this.pageSettings, this.hasFolloup, this.haveSubmited);
            this.haveSubmited += 1;
            this.hasFolloup = false;
        } else {
            document.getElementById("errorFeedback").innerHTML = "<font color=red>No response was recorded because you did not answer all the questions</font>";
        }
    }

    authorSubmitResponses() {
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            let value = current.answerBox.recordCurrentResponse(this.response);
            if (value !== ResponseResult.blank) {
                addToResponseIds(this.response, current.element.id);
            }
            for (var x = 0; x < current.followUpAreas.length; x++) {
                value = current.followUpAreas[x].answerBox.recordCurrentResponse(this.response);
                if (value !== ResponseResult.blank) {
                    addToResponseIds(this.response, current.followUpAreas[x].element.id);
                }
            }
        }
    }

    displayFeedback() {
        if (this.willDisplayFeedback) {
            document.getElementById("helpfulFeedback").innerHTML = giveFeedback(this.response.typesIncorrect, this.feedbackByType);
        }
    }

    checkFollowUp(current) {
        for (var x = 0; x < current.followUpAreas.length; x++) {
            let correctness = current.followUpAreas[x].answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer, current.followUpAreas[x].element.id);
            this.listOfCorrectAnswers.push(correctness);
        }
    }

    checkAnswers() {
        this.response.taskQuestionIds = [];
        this.listOfCorrectAnswers = [];
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            let correctness = current.answerBox.checkCurrentResponse(this.response, this.unsureShowsCorrectAnswer, current.element.id);
            this.listOfCorrectAnswers.push(correctness);
            if (checkIfShouldAddFollowupQ(correctness)) {
                current.addFollowupQuestions();
                if (current.followUpAreas.length > 0) {
                    this.hasFolloup = true;
                }
            }
            this.checkFollowUp(current);
        }
    }

    createQuestionAreaElement() {
        let outerQuestionSetNode = document.createElement('form');
        outerQuestionSetNode.setAttribute('action', '#');
        outerQuestionSetNode.setAttribute('method', 'post');
        outerQuestionSetNode.setAttribute('id', 'questionSetForm');

        let questionElement = document.createElement('div');
        questionElement.setAttribute('id', 'questionSet');

        this.questionAreaDisp = new buildQuestionAreas(this.imageTaskJson.taskQuestions, this.response);
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            if (this.isAuthor) {
                this.questionAreaDisp[i].addFollowupQuestions();
            }
            questionElement.appendChild(this.questionAreaDisp[i].element);
        }
        outerQuestionSetNode.appendChild(questionElement);
        return outerQuestionSetNode;
    }

    createCanvasElement() {
        console.log('we in cCE');
        let canvasElement = document.createElement('div');
        canvasElement.setAttribute('id', 'canvasArea');
        console.log(canvasElement);
        let canvas;
        if (this.imageUrl === "noMoreQuestions") {
            canvas = new PageImage("../images/ParLogo.png", this.canvasName);

        } else {
            canvas = new PageImage(this.imageUrl, this.canvasName);
            console.log(canvas);
        }
        console.log(this.imageUrl);
        canvasElement.appendChild(canvas.element);
        canvas.loadImage();
        console.log(canvasElement);
        console.log(typeof (canvasElement));
        return canvasElement;
    }

    createSubmitButtonElement() {
        let outerSubmitRowElement = document.createElement('div');
        outerSubmitRowElement.classList.add('row');

        let submitButtonColumnHandlerElement = document.createElement('div');
        submitButtonColumnHandlerElement.classList.add('col-12');
        submitButtonColumnHandlerElement.classList.add('text-center');

        let showElement = document.createElement('div');
        showElement.classList.add('show');

        let tagElement = document.createElement('i');
        tagElement.setAttribute('id', 'submitButtonTag');

        let submitButtonElement = document.createElement('button');
        submitButtonElement.setAttribute('type', 'button');
        submitButtonElement.classList.add('btn');
        submitButtonElement.classList.add('btn-primary');
        submitButtonElement.setAttribute('id', 'submitButton');
        submitButtonElement.setAttribute('onclick', 'completeDisplay.pageDisplay.imageTaskDisplay.submitAnswers()');
        submitButtonElement.textContent = 'Submit';

        let authorButtonElement = document.createElement('button');
        authorButtonElement.setAttribute('type', 'button');
        authorButtonElement.classList.add('btn');
        authorButtonElement.classList.add('btn-primary');
        authorButtonElement.classList.add('hide');
        authorButtonElement.setAttribute('id', 'authorReviewSubmitButton');
        authorButtonElement.setAttribute('onclick', 'completeDisplay.pageDisplay.authorSubmitFinal()');
        authorButtonElement.textContent = 'Add Questions To Pool';


        let nextQuestionButtonElement = document.createElement('button');
        nextQuestionButtonElement.setAttribute('type', 'button');
        nextQuestionButtonElement.classList.add('fas');
        nextQuestionButtonElement.classList.add('fa-arrow-circle-right');
        nextQuestionButtonElement.classList.add('btn');
        nextQuestionButtonElement.classList.add('btn-outline-dark');
        nextQuestionButtonElement.setAttribute('id', 'nextQuestionButton');
        nextQuestionButtonElement.setAttribute('onclick', 'completeDisplay.nextQuestion()');


        tagElement.appendChild(submitButtonElement);
        tagElement.appendChild(authorButtonElement);
        tagElement.appendChild(nextQuestionButtonElement);


        let errorFeedbackElement = document.createElement('i');
        errorFeedbackElement.setAttribute('id', 'errorFeedback');

        showElement.appendChild(tagElement);
        showElement.appendChild(errorFeedbackElement);


        let idRowElement = document.createElement('div');
        idRowElement.classList.add('row');

        let idColElement = document.createElement('div');
        idColElement.classList.add('col-12');
        idColElement.classList.add('text-center');

        let idElement = document.createElement('i');
        idElement.setAttribute('id', 'Ids');

        idColElement.appendChild(idElement);
        idRowElement.appendChild(idColElement);

        submitButtonColumnHandlerElement.appendChild(showElement);
        submitButtonColumnHandlerElement.appendChild(idRowElement);

        outerSubmitRowElement.appendChild(submitButtonColumnHandlerElement);

        return outerSubmitRowElement;
    }


    createImageTaskElement() {
        console.log('we in cITE in ITD');

        let outerImageTaskNode = document.createElement('div');
        outerImageTaskNode.classList.add('row');

        console.log('we here oITN');

        let canvasNode = this.createCanvasElement();

        console.log(canvasNode);
        console.log(typeof (canvasNode));

        let canvasElementHandler = document.createElement('div');
        canvasElementHandler.classList.add('col-6');
        canvasElementHandler.classList.add('imgCenter');
        canvasElementHandler.appendChild(canvasNode);

        console.log('we here cEH');

        let questionAreaNode = this.createQuestionAreaElement();
        let questionAreaElementHandler = document.createElement('div');
        questionAreaElementHandler.classList.add('col-4');
        let questionTitleElement = document.createElement('h1');
        questionTitleElement.classList.add('text-center');
        questionTitleElement.textContent = "Question Set";

        console.log('we here qTE');

        let feedbackElement = document.createElement('i');
        feedbackElement.setAttribute('id', 'helpfulFeedback');
        feedbackElement.classList.add('text-center');

        console.log('we here fE');

        let submitButtonElement = this.createSubmitButtonElement();

        console.log('we here sBE');

        questionAreaElementHandler.appendChild(questionTitleElement);
        questionAreaElementHandler.appendChild(questionAreaNode);
        questionAreaElementHandler.appendChild(feedbackElement);
        questionAreaElementHandler.appendChild(submitButtonElement);

        console.log('we here qAEH');

        let spaceNode0 = document.createElement('div');
        spaceNode0.classList.add('col-1');
        let spaceNode1 = document.createElement('div');
        spaceNode1.classList.add('col-1');

        console.log('we here sN0/1');

        outerImageTaskNode.appendChild(spaceNode0);

        console.log('we here append sN1/2');

        console.log(outerImageTaskNode);
        console.log(typeof (outerImageTaskNode));

        outerImageTaskNode.appendChild(canvasElementHandler);

        console.log(outerImageTaskNode);
        console.log(typeof (outerImageTaskNode));

        outerImageTaskNode.appendChild(questionAreaElementHandler);

        console.log(outerImageTaskNode);
        console.log(typeof (outerImageTaskNode));

        outerImageTaskNode.appendChild(spaceNode1);

        console.log(outerImageTaskNode);
        console.log(typeof (outerImageTaskNode));

        return outerImageTaskNode;
    }

    lockInCorrectAnswers() {
        for (var i = 0; i < this.questionAreaDisp.length; i++) {
            let current = this.questionAreaDisp[i];
            current.answerBox.inputTextbox.value = current.answerBox.correctResponse;
            disableElement(current.answerBox.inputTextbox);
            for (var x = 0; x < current.followUpAreas.length; x++) {
                current.followUpAreas[x].answerBox.inputTextbox.value = current.followUpAreas[x].answerBox.correctResponse;
                disableElement(current.followUpAreas[x].answerBox.inputTextbox);
            }
        }
    }

}

function addUnsureToAnswers(questionObjectList) {
    let questionAreaList = [];
    for (let questionObject of questionObjectList) {
        questionObject.possibleAnswers.push(ResponseResult.unsure);
        addUnsureToAnswers(questionObject.followupQuestions);
    }
}

function checkIfShouldAddFollowupQ(correctness) {
    if (correctness === ResponseResult.correct) {
        return true;
    } else {
        return false;
    }
}

function addToResponseIds(response, id) {
    if (!(response.taskQuestionIds.includes(id))) {
        response.addToQuestionIds(id);
    }
}

function submitResponse(response, isAuthor, pageSettings) {
    let newResponse = {
        userId: response.userId,
        taskQuestionIds: response.taskQuestionIds,
        responseTexts: response.responseTexts
    };

    if (isAuthor) {
        submitToAPI("api/submitAuthorImageTaskResponse", newResponse, pageSettings.showScore, pageSettings.scoreType, this.userID);
    } else {
        submitToAPI("api/recordResponse", newResponse, pageSettings.showScore, pageSettings.scoreType, this.userID);
    }
}

function giveFeedback(typesSeenForFeedback, feedbackByType) {
    var feedbackString = "";
    if (typesSeenForFeedback.length > 0) {
        feedbackString = "Feedback: ";
    }
    for (var i = 0; i < typesSeenForFeedback.length; i++) {
        var type = typesSeenForFeedback[i];
        var response = feedbackByType[type];
        if (i < typesSeenForFeedback.length - 1) {
            response += ", ";
        }
        feedbackString += response;
    }

    return feedbackString;
}

function sendResponse(response, ableToResubmitAnswers, isAuthor, pageSettings, hasFollowup, timesSubmitted) {
    submitResponse(response, isAuthor, pageSettings);

    if (!ableToResubmitAnswers) {
        if (hasFollowup) {
            if (timesSubmitted > 0) {
                document.getElementById("submitButton").classList.add("hide");
            }
        } else {
            document.getElementById("submitButton").classList.add("hide");
        }
    }

    document.getElementById("errorFeedback").innerHTML = "<font color=\"#663399\"> Response recorded</font>";

    return true;
}

function checkIfCanContinu(canGiveNoAnswer, listOfCorrectAnswers) {
    if (!canGiveNoAnswer) {
        if (listOfCorrectAnswers.includes("")) {
            return false;
        } else {
            return true;
        }
    } else {
        return true;
    }
}
