//
//Canvas functions
//
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



//
//functions to create the questions
//
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
    document.getElementById("Ids").innerHTML = questionIDs.toString();
}

function setUserId() {
    document.getElementById("UserId").innerHTML = "&nbsp" + sendUserId();
}

//Gets JSON, reads it, and returns it
function readJson(url) {
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

//display function for showing previously generated HTML of questions onscreen.
function displayQuestion(displayHTML) {
    document.getElementById("questionSet").innerHTML += displayHTML;
}

//Creates radio question based on question given and difficulty for the html.
function createRadioQuestion(json, currentAmountOfQuestions) {
    var question = "<p>" + json.questionText + "</p>";
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question += '<br> <input type="radio" name="' + ("q" + currentAmountOfQuestions) + '" value="';
        question = question + json.possibleAnswers[i] + '">' + json.possibleAnswers[i];
    }
    question += '<br> <input type="radio" name="I do not know" value="Unsure"';
    question += '<br> <i id="questionCorrect"' + (currentAmountOfQuestions) + '></i>';
    return question;
}

//Creates datalist dropdown based on a question given for the html.
function createDatalistDropdown(json, currentAmountOfQuestions) {
    var i;
    var question = "<p>" + json.questionText + '</p> <input id="' + ("q" + (currentAmountOfQuestions)) + '" list="' + ("list" + currentAmountOfQuestions) + '"/> <datalist id="' + ("list" + currentAmountOfQuestions) + '">';
    for (i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option id= "option' + i + '" value="' + json.possibleAnswers[i] + '"/>';
    }
    question += '<option id= "optionUnsure" value="Unsure"/>';
    question += '</datalist>';
    question += '<i id="' + "questionCorrect" + (currentAmountOfQuestions) + '"></i>';
    return question;
}
//Creates select dropdown based on a question given for the html.
function createSelectDropdown(json, currentAmountOfQuestions) {
    var question = "<p>" + json.questionText + '</p> <select id="' + ("q" + (currentAmountOfQuestions)) + '">';
    for (i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option id="option' + i + '">' + json.possibleAnswers[i] + '</option>';
    }
    question += '<option id="optionUnsure">Unsure</option>';
    question += '</select>';
    question += '<i id="' + "questionCorrect" + (currentAmountOfQuestions) + '"></i>';
    return question;
}


//
//score functions
//
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
        //generateScoreStringByType();
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

function generateScoreStringByType() {
    var visString = " ";
    var visJSON = readJson("api/getScoreStringByType?userId=" + sendUserId());
    for (var key in visJSON) { //key search
        if (visJSON.hasOwnProperty(key)) {
            let value = visJSON[key];
            visString += "&nbsp <i class=black>" + key + ": </i>";
            for (let i = 0; i < value.length; i++) { //value search on string
                if (value[i] == "O") {
                    visString += '<i class="fas fa-check-circle green"></i>';
                } else if (value[i] == "X") {
                    visString += '<i class="fas fa-times-circle red"></i>';
                } else if (value[i] == "_") {
                    visString += '<i class="fas fa-circle"></i>';
                } else {
                    visString += '<i class="fas fa-minus-circle yellow"></i>';
                }
            }
            visString += '<br />';
        }
    }
    displayScoreBreakdownByVisualization(visString);
}

function displayScoreBreakdownByVisualization(visualizationString) {
    if (showScore) {
        document.getElementById("score").innerHTML = visualizationString;
    }
}


function displayScoreBreakdown(breakdownString) {
    if (showScore) {
        document.getElementById("score").innerHTML = " " + breakdownString;
    }
}



//
//Answering questions and feedback functions
//
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
            return '<font color=\"#663399\">' + "The correct answer is " + rightAnwser + '</font>';
        } else {
            return '<font color="#663399">Your answer is: Unsure</font>';
        }
    }
    if (value == "blank") {
        return "";
    }
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

function reEnableSubmit() {
    document.getElementById("submitButtonTag").innerHTML = " <button type=\"button\" class=\"btn btn-primary\" id=\"submitButton\" onclick=\"checkAnswers()\">" +
        "Submit" + "</button>";
}

function disableSubmit() {
    document.getElementById("submitButtonTag").innerHTML = " ";
}



//
//Response functions
//
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

function createResponses() {
    var object = createResponseJson();
    submitToAPI("api/recordResponse", object);
}

function disableField(elementToCheck, elementToToggle) {

    if (document.getElementById(elementToCheck).value == "unsure" || document.getElementById(elementToCheck).value == "Unsure") {
        document.getElementById(elementToToggle).disabled = true;
    } else {
        document.getElementById(elementToToggle).disabled = false;
    }
}


//
//for testing purposes only
//
function testSetVariables() {
    responsesGivenText = ["Lateral", "ligament", "Unsure"];
    questionIDs = ["PlaneQ1", "StructureQ1", "ZoneQ1"];
    userID = "Hewwo123";
}

function testGenerateReponseJSON() {
    testSetVariables();
    return createResponseJson();
}
