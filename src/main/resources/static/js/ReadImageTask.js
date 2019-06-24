//When the page has fully loaded, execute the canvasApp
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
        var imageTaskJSON = readJson("api/nextImageTask?userId="+sendUserId());
        pageDisplay(imageTaskJSON);
        var displayURLThyme = imageTaskJSON.imageUrl.split('\\').pop().split('/').pop();
        imageSource= "./images/" + displayURLThyme;


        image = new Image();
        image.onload = function() {callback()};
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


var amountOfQuestions = 0;
var QuestionAnswers = [];
var QuestionIDs = [];
var UserID= null;

function sendUserId() {
    if (UserID != null) {
        return UserID;
    } else {
        return "Student";
    }
}

function changeUserID(newID){
    UserID=newID;
}

function getNumberOfQuestions() {
    return amountOfQuestions;
}

function getQuestionAnswers() {
    return QuestionAnswers;
}

function getQuestionIDs() {
    return QuestionIDs;
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
        question += '<br> <input type="radio" name="' + ("q" + (getNumberOfQuestions())) + '" value="';
        question = question + json.possibleAnswers[i] + '">' + json.possibleAnswers[i] + '<br> <i id="' + "questionCorrect" + (getNumberOfQuestions()) + '"></i>';
    }
    return question;
}

//Creates fill in question based on question given and difficulty for the html.
function createFillIn(json) {
    var question = "<p>" + json.questionText + '</p> <input name="' + ("q" + (getNumberOfQuestions())) + '" list="' + ("list" + getNumberOfQuestions()) + '"/> <datalist id="' + ("list" + getNumberOfQuestions()) + '">';
    for (var i = 0; i < json.possibleAnswers.length; i++) {
        question = question + '<option value="' + json.possibleAnswers[i] + '"/>';
    }
    question += '</datalist>';
    question += '<i id="' + "questionCorrect" + (amountOfQuestions) + '"></i>';
    return question;
}

//caller function for changing the questions (and image task).
function changeQuestions() {
    clearPage();
    canvasApp();
}
/** might be needed later, commented out rather than deleted.
function generateImageURL(imageURL) {
    //var displayURLThyme = imageURL.split('\\').pop().split('/').pop();
    //displayURLThyme = displayURLThyme.substr(0, displayURLThyme.length - 1);
    var displayURLThyme = "images/demoEquine02.jpg";
    //imageURL = imageURL.substr(0, imageURL.length - 1);
    var displayURL = '<img th:src="@{' + displayURLThyme + '}" class="imgCenter"/>';
    console.log(displayURL);
    //console.log(imageURL);
    console.log(displayURLThyme);
    return displayURL;
}

function displayImageURL(imageURL) {
    document.getElementById('image').innerHTML = imageURL;
}
**/
function setCurrentScore(){
    var url= "api/calcScore?userId="+sendUserId();
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    //can be -1 currently if User is new
    var score=request.response;
    console.log(score);
    if(score==-1) {
        document.getElementById("score").innerHTML = "0";
    } else {
        document.getElementById("score").innerHTML = score;
    }
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
    QuestionIDs= [];
}
//Removes all text from the screen at id questionSet to prep for next image task.
function clearPage() {
    document.getElementById('questionSet').innerHTML = " ";
    amountOfQuestions = 0;
}
