window.addEventListener("load", canvasApp, false);

function canvasSupport() {
    return Modernizr.canvas;
}

function canvasApp() {
    //Check to see if the canvas has a context
    if (!canvasSupport()) {
        return; //Canvas not supported so exit the function
    }

    var image;
    var imageSource;

    function loadImages(images, imageSources, callback) {
        try {
            var imageTaskJSON = readJson("api/nextImageTask?userId=" + sendUserId());

        } catch (Exception) {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }

        pageDisplay(imageTaskJSON);
        imageSource = imageTaskJSON.imageUrl;

        image = new Image();
        image.onload = function () {
            callback()
        };
        image.src = imageSource;

    }

    //Setup the canvas object
    var theCanvas = document.getElementById("myCanvas"); //get the canvas element
    var context = theCanvas.getContext("2d"); //get the context
    var canvasHeight = theCanvas.height; //get the heigth of the canvas
    var canvasWidth = theCanvas.width; //get the width of the canvas
    var canvasColor = "white"; // set the default canvas bg color

    function clearCanvas(canvasColor) {
        context.fillStyle = canvasColor;
        context.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    function drawCanvas() {
        clearCanvas(canvasColor);
        context.drawImage(image, 0, 0, canvasWidth, canvasHeight);
    }

    loadImages(image, imageSource, function () {
        drawCanvas()
    });

}

function sendUserId() {
    if (userID != null) {
        return userID;
    } else {
        return "Student";
    }
}

//Gets JSON, reads it, and returns it
function readJson(url) {
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}
//generates question for html based on the question given (the JSON)
function generateQuestion(question) {
    var difficultyStr = createFillIn(question);
    amountOfQuestions++;

    questionAnswers.push(question.correctAnswer);
    questionIDs.push(question.id);
    questionTypes.push(question.type);
    displayQuestion(difficultyStr);
}

//display function for showing previously generated HTML of questions onscreen.
function displayQuestion(displayHTML) {
    document.getElementById("questionSet").innerHTML += displayHTML;
}

//Creates radio question based on question given and difficulty for the html.
function createRadioQuestion(json) {
    var question = "<p>" + json.questionText + "</p>";
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question += '<br> <input type="radio" name="' + ("q" + amountOfQuestions) + '" value="';
        question = question + json.possibleAnswers[i] + '">' + json.possibleAnswers[i];
    }
    question += '<br> <input type="radio" name="I do not know" value="Unsure"';
    question += '<br> <i id="questionCorrect"' + (amountOfQuestions) + '></i>';
    return question;
}

//Creates fill in question based on question given and difficulty for the html.
function createFillIn(json) {
    var question = "<p>" + json.questionText + '</p> <input name="' + ("q" + (amountOfQuestions)) + '" list="' + ("list" + amountOfQuestions) + '"/> <datalist id="' + ("list" + amountOfQuestions) + '">';
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option value="' + json.possibleAnswers[i] + '"/>';
    }
    question += '<option value="Unsure"/>';
    question += '</datalist>';
    question += '<i id="' + "questionCorrect" + (amountOfQuestions) + '"></i>';
    return question;
}

function generateScoreLevel() {

}

function generateSinglescore() {
    var url = "api/calcScore?userId=" + sendUserId();
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    //can be -1 currently if User is new
    var score = request.response;
    if (score == -1) {
        document.getElementById("score").innerHTML = "&nbsp" + "0";
    } else {
        document.getElementById("score").innerHTML = "&nbsp" + score;
    }
}

function setCurrentScore() {
    if (scoreType == "ByType") {
        generateScoreByType();
    } else if (scoreType == "SingleScore") {
        generateSinglescore();
    } else if (scoreType == "Level") {
        generateScoreLevel();
    }
}

function generateScoreByType() {
    //80-100 green
    //79-50 orange
    //49-0 red
    var breakdownString = " ";
    var scoreJson = readJson("api/calcScoreByType?userId=" + sendUserId());
    for (var key in scoreJson) {
        if (scoreJson.hasOwnProperty(key)) {
            let value = scoreJson[key];
            if (value >= 80) {
                breakdownString += "<i class=black>" + key + ":</i> <i class=green>" + value + "<i>";
            } else if (value <= 79 && value >= 50) {
                breakdownString += "<i class=black>" + key + ":</i> <i class=orange>" + value + "<i>";
            } else if (value <= 49) {
                breakdownString += "<i class=black>" + key + ":</i> <i class=red>" + value + "<i>";
            }
        }
        breakdownString += "<br />";
    }
    displayScoreBreakdown(breakdownString);

}

function displayScoreBreakdown(breakdownString) {
    if (showScore) {
        document.getElementById("score").innerHTML = " " + breakdownString;
    }
}

function setUserId() {
    document.getElementById("UserId").innerHTML = "&nbsp" + sendUserId();
}
//Calls generateQuestion on the JSON object for the question at ith index
function pageDisplay(imageTaskJSON) {
    clearQuestionIDs();
    if (showScore) {
        document.getElementById("scoreTag").innerHTML = "<b class=\"size20\">Knowledge Base: </b>";
        setCurrentScore();
    }
    //Displays the questions at the tags
    for (var i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        generateQuestion(imageTaskJSON.taskQuestions[i]);
    }
}
//Clears question IDs from working image task.
function clearQuestionIDs() {
    questionIDs = [];
}
//Removes all text from the screen at id questionSet to prep for next image task.
function clearPage() {
    document.getElementById('questionSet').innerHTML = " ";
    amountOfQuestions = 0;
}

//Checks the answers given in the questions against the record of what is correct/incorrect.
function checkAndRecordAnswers() {
    var form = document.getElementById("form1");
    for (var i = 0; i < amountOfQuestions; i++) {
        var currentName = "q" + i;
        var currentAnswer = form[currentName].value;

        responsesGivenText.push(currentAnswer);

        var isCorrect;
        console.log("Answer " + i + " " + questionAnswers[i]);
        if (currentAnswer == questionAnswers[i]) {
            isCorrect = "Correct";
        } else if (currentAnswer == "Unsure") {
            isCorrect = "Unsure";
        } else if (currentAnswer == "") {
            if (!canGiveNoAnswer) {
                responsesGivenText.pop();
            } else {
                isCorrect = "Incorrect";
                addToTypesSeenForFeedback(questionTypes[i]);
            }
        } else {
            isCorrect = "Incorrect";
            addToTypesSeenForFeedback(questionTypes[i]);
        }
        var displayAreaName = "questionCorrect" + i;
        document.getElementById(displayAreaName).innerHTML = displayCheck(isCorrect, questionAnswers[i], unsureShowsCorrectAnswer);
    }
    generateFeedback();
}

function addToTypesSeenForFeedback(type) {
    if (!typesSeenForFeedback.includes(type)) {
        typesSeenForFeedback.push(type);
    }
}

function generateFeedback() {
    if (typesSeenForFeedback.length > 0) {
        document.getElementById("helpfulFeedback").innerHTML = "Feedback: ";
    }
    for (var i = 0; i < typesSeenForFeedback.length; i++) {
        var type = typesSeenForFeedback[i];
        var response = feedbackByType[type];
        if (i < typesSeenForFeedback.length - 1) {
            response += ", ";
        }
        document.getElementById("helpfulFeedback").innerHTML += response;
    }
}

function clearFeedback() {
    document.getElementById("helpfulFeedback").innerHTML = " ";
}

//Displays the value of right/wrong based on the previous function's input value.
function displayCheck(value, rightAnwser, unsureShowsCorrectAnswerHere) {
    if (value == "Correct") {
        return '<font color=\"green\">Your answer is: ' + value + '</font>';
    }

    if (value == "Incorrect") {
        return '<font color=\"red\">Your answer is: ' + value + '</font>';
    }

    if (value == "Unsure") {
        if (unsureShowsCorrectAnswerHere == true) {
            return '<font color=\"#663399\">Your answer is: ' + value + ".    " + 'The answer is ' + rightAnwser + '</font>';
        } else {
            return '<font color=\"#663399\">Your answer is: ' + value + '</font>';
        }
    }
}
//Clears the answers from the page.
function clearQuestionAnswers() {
    responsesGivenText = [];
    questionAnswers= [];
}

function reEnableSubmit() {
    document.getElementById("submitButtonTag").innerHTML = " <button type=\"button\" class=\"btn btn-primary\" id=\"submitButton\" onclick=\"checkAnswers()\">" +
        "Submit" + "</button>";
}

function disableSubmit() {
    document.getElementById("submitButtonTag").innerHTML = " ";
}

//  function toggleShowState(toggableElement) {
//     var changeElement = document.getElementById(toggableElement).classList;
//
//     if (changeElement.contains("show") || changeElement.style.display == "block") {
//         changeElement.remove("show");
//         changeElement.add("hide");
//     } else if (changeElement.contains("hide")) {
//         changeElement.remove("hide");
//         changeElement.add("show");
//     }
// }


function createResponseJson() {
    var newResponse;
    if (userID != null) {
        newResponse = {
            userId: userID,
            taskQuestionIds: questionIDs,
            responseTexts: responsesGivenText
        };
    } else {
        newResponse = {
            userId: "Student",
            taskQuestionIds: questionIDs,
            responseTexts: responsesGivenText
        };
    }

    console.log(questionIDs);

    return newResponse;
}

function submitToAPI(url, objectToSubmit) {
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(objectToSubmit));
    request.onreadystatechange = function () {
        if (request.status === 200) {
            setCurrentScore();
        } else {
            window.onerror = function (msg) {
                location.replace('/error?message=' + msg);
            }
        }
    };
}

function clearQuestionCorrectnessResponses() {
    for (var i = 0; i < amountOfQuestions; i++) {
        var displayAreaName = "questionCorrect" + i;
        document.getElementById(displayAreaName).innerHTML = " ";
    }

}

function checkForAnswers() {
    if (numberOfQuestionsAnswered == amountOfQuestions) {
        return true;
    } else {
        document.getElementById("errorFeedback").innerHTML = '<font color=red>Must submit answers to continue</font>';
        return false;
    }
}

function logout() {
    userID = null;
    return location.replace('/login');
}

function getSettings() {
    try {
        var settings = readJson("api/getSettings");

    } catch (Exception) {
        window.onerror = function (msg) {
            location.replace('/error?message=' + msg);
        }
    }

    unsureShowsCorrectAnswer = settings.unsureShowsCorrectAnswer;
    feedbackByType = settings.feedbackByType;
    ableToResubmitAnswers = settings.ableToResubmitAnswers;
    scoreType = settings.scoreType;
    showScore = settings.showScore;
    mustSubmitAnswersToContinue = settings.mustSubmitAnswersToContinue;
    canGiveNoAnswer = settings.canGiveNoAnswer;
}

//for testing purposes only
function testSetVariables() {
    responsesGivenText = ["Lateral", "ligament", "Unsure"];
    questionIDs = ["PlaneQ1", "StructureQ1", "ZoneQ1"];
    userID = "Hewwo123";
}

function testGenerateReponseJSON() {
    testSetVariables();
    return createResponseJson();
}
