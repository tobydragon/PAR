//When the page has fully loaded, execute the eventWindowLoaded function
window.addEventListener("load", eventWindowLoaded, false);

//-----------------------------------------------------------
//eventWindowLoaded()
//Called when the window has been loaded it then calls the canvasapp()
function eventWindowLoaded() {
    canvasApp();
} // eventWindowLoaded()

//-----------------------------------------------------------
//canvasSupport()
//Check for Canvas Support using modernizr.js
function canvasSupport() {
    return Modernizr.canvas;
} // canvasSupport()

//-----------------------------------------------------------
//canvasApp()
//The function where ALL our canvas code will go
function canvasApp() {
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Canvas Support */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    //-----------------------------------------------------------
    //Check to see if the canvas has a context
    if (!canvasSupport()) {
        return; //Canvas not supported so exit the function
    }

    var images = [];
    // declare an array for image sources and assign the image sources
    var imageSources = []; //imageSource

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // create and load image objects into an array
    // based on an image source array
    function loadImages(images, imageSources, callback) {
        var imageTaskJSON = readJson("api/nextImageTask");
        pageDisplay(imageTaskJSON);
        var displayURLThyme = imageTaskJSON.imageUrl.split('\\').pop().split('/').pop();
        imageSources.push("./images/" + displayURLThyme);

        var loadedImages = 0;
        //- - - - - - - - - - - - - - - - - - - - -
        // for each imageSource
        for (var src = 0; src < imageSources.length; src++) {
            //- - - - - - - - - - - - - - - - - - - - -
            //create a new image object
            images[src] = new Image();
            //- - - - - - - - - - - - - - - - - - - - -
            //load the image
            images[src].onload = function () {
                if (++loadedImages >= imageSources.length) {
                    callback(images);
                }; //if
            } //onload()
            //- - - - - - - - - - - - - - - - - - - - -
            //set the image source
            images[src].src = imageSources[src];
        } //for
    } //loadimages()

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* Canvas Variables */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    //-----------------------------------------------------------
    //Setup the canvas object
    var theCanvas = document.getElementById("myCanvas"); //get the canvas element
    var context = theCanvas.getContext("2d"); //get the context
    var canvasHeight = theCanvas.height; //get the heigth of the canvas
    var canvasWidth = theCanvas.width; //get the width of the canvas
    var canvasColor = "white"; // set the default canvas bg color


    function drawBGImage() {
        //draw the bg image to fill the canvas
        context.drawImage(images[0], 0, 0, canvasWidth, canvasHeight);
    } //drawBGImage

    function clearCanvas(canvasColor) {
        // set a fill style of white
        context.fillStyle = canvasColor;
        // fill the while canvas with the fill style
        context.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    function drawCanvas() {
        //--------------------------------------------
        //1. clear and setup the canvas
        //clear the canvas
        clearCanvas(canvasColor);
        //draw the bg image
        drawBGImage();
        //write the frame counter
        //writeFrameCounter(frameCounter);
    } //drawCanvas()

    function gameLoop() {
        //get the next animation frame
        requestAnimationFrame(gameLoop);
        //draw the canvas
        drawCanvas();
    } //gameLoop()

    loadImages(images, imageSources, function (images) {
        //call game loop
        gameLoop();
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
    var url= "api/calcScore?userId=Student";
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);
    console.log(request.response);
    document.getElementById("score").innerHTML= request.response;
    console.log("Hello?");
}

//Calls generateQuestion on the JSON object for the question at ith index
function pageDisplay(imageTaskJSON) {
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
