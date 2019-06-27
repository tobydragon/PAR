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
        var imageTaskJSON = readJson("api/nextImageTask?userId=" + sendUserId());
        pageDisplay(imageTaskJSON);
        var displayURLThyme = imageTaskJSON.imageUrl.split('\\').pop().split('/').pop();
        imageSource = "./images/" + displayURLThyme;


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
    if (UserID != null) {
        return UserID;
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

    QuestionAnswers.push(question.correctAnswer);
    QuestionIDs.push(question.id);
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

function setCurrentScore() {
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

function generateScoreBreakdown() {
    //80-100 green
    //79-50 orange
    //49-0 red
    var breakdownString = "";
    var scoreJson = {
        "scores": [
            {
                "type": "plane",
                "num": 90
            },
            {
                "type": "struct",
                "num": 30
            },
            {
                "type": "attach",
                "num": 40
            },
            {
                "type": "zone",
                "num": 72
            }
        ]
    }
    for (var i = 0; i < scoreJson.scores.length; i++) {
        if (scoreJson.scores[i].num >= 80) {
            breakdownString += "<i class=black>" + scoreJson.scores[i].type + ":</i> <i class=green>" + scoreJson.scores[i].num + "</i>";
        } else if (scoreJson.scores[i].num <= 79 && scoreJson.scores[i].num >= 50) {
            breakdownString += "<i class=black>" + scoreJson.scores[i].type + ":</i> <i class=orange>" + scoreJson.scores[i].num + "</i>";
        } else if (scoreJson.scores[i].num <= 49) {
            breakdownString += "<i class=black>" + scoreJson.scores[i].type + ":</i> <i class=red>" + scoreJson.scores[i].num + "</i>";
        }
        breakdownString += "<br />";
    }
    displayScoreBreakdown(breakdownString);



}

function displayScoreBreakdown(breakdownString) {
    document.getElementById("score").innerHTML = " " + breakdownString;
}

function setUserId() {
    document.getElementById("UserId").innerHTML = "&nbsp" + sendUserId();
}
//Calls generateQuestion on the JSON object for the question at ith index
function pageDisplay(imageTaskJSON) {
    clearQuestionIDs();
    setCurrentScore();
    //Displays the questions at the tags
    for (var i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        generateQuestion(imageTaskJSON.taskQuestions[i]);
    }
}
//Clears question IDs from working image task.
function clearQuestionIDs() {
    QuestionIDs = [];
}
//Removes all text from the screen at id questionSet to prep for next image task.
function clearPage() {
    document.getElementById('questionSet').innerHTML = " ";
    amountOfQuestions = 0;
}

//Checks the answers given in the questions against the record of what is correct/incorrect.
function checkAndRecordAnswers() {
    var form = document.getElementById("form1")
    for (var i = 0; i < amountOfQuestions; i++) {
        var currentName = "q" + i;
        var currentAnswer = form[currentName].value;

        responsesGivenText.push(currentAnswer);

        var isCorrect;
        if (currentAnswer == QuestionAnswers[i]) {
            isCorrect = "Correct";
        } else if (currentAnswer == "Unsure") {
            isCorrect = "Unsure";
        } else {
            isCorrect = "Incorrect";
        }
        var displayAreaName = "questionCorrect" + i;
        document.getElementById(displayAreaName).innerHTML = displayCheck(isCorrect, QuestionAnswers[i]);
    }
}

//Displays the value of right/wrong based on the previous function's input value.
function displayCheck(value, rightAnwser) {
    if (value == "Correct") return '<font color=\"green\">Your answer is: ' + value + '</font>';
    if (value == "Incorrect") return '<font color=\"red\">Your answer is: ' + value + '</font>';
    if (value == "Unsure") return '<font color=\"#663399\">Your answer is: ' + value + ".    " + 'The answer is ' + rightAnwser + '</font>';
}
//Clears the answers from the page.
function clearQuestionAnswers() {
    responsesGivenText = [];
}
/** not needed right now, so commented out, but shows the toggable state of an element on a page.
 function toggleShowState(toggableElement) {
    var changeElement = document.getElementById(toggableElement).classList;

    if (changeElement.contains("show") || changeElement.style.display == "block") {
        changeElement.remove("show");
        changeElement.add("hide");
    } else if (changeElement.contains("hide")) {
        changeElement.remove("hide");
        changeElement.add("show");
    }
}
 **/

function createResponseJson() {
    var newResponse;
    if (UserID != null) {
        newResponse = {
            userId: UserID,
            taskQuestionIds: QuestionIDs,
            responseTexts: responsesGivenText
        };
    } else {
        newResponse = {
            userId: "Student",
            taskQuestionIds: QuestionIDs,
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
            //setCurrentScore();
            generateScoreBreakdown();
        } else {
            window.location.replace("/serverError");
        }
    };
}

function logout() {
    UserID = null;
    return location.replace('/login');
}

//for testing purposes only
function testSetVariables() {
    responsesGivenText = ["Lateral", "ligament", "Unsure"];
    QuestionIDs = ["PlaneQ1", "StructureQ1", "ZoneQ1"];
    UserID = "Hewwo123";
}

function testGenerateReponseJSON() {
    testSetVariables();
    return createResponseJson();
}
